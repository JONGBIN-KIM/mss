package com.mss.assignments.application.service

import com.mss.assignments.application.dto.ProductDTO
import com.mss.assignments.application.exception.CategoryNotFoundException
import com.mss.assignments.application.exception.ProductNotFoundException
import com.mss.assignments.application.mapper.ModelMapper
import com.mss.assignments.domain.model.Product
import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import com.mss.assignments.infrastructure.persistence.entity.ProductEntity
import com.mss.assignments.infrastructure.persistence.repository.CategoryRepository
import com.mss.assignments.infrastructure.persistence.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val cacheManager: CacheManager
) {
    companion object {
        private const val CACHE_EXPIRATION_MINUTES = 5L
        private const val CACHE_REFRESH_CHANCE = 0.2
        private const val CACHE_EXPIRATION_KEY = "expiration:price:brand"
        private const val BRAND_CATEGORY_LOWEST_PRICE_KEY = "price:brand:category:lowest"
        private const val CACHE_KEY_LOWEST = "category:%d:price:lowest"
        private const val CACHE_KEY_HIGHEST = "category:%d:price:highest"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // 구현1에 사용할 함수
    @Transactional(readOnly = true)
    fun getLowestPriceProducts(): Map<String, Map<String, Any>> {
        val categories = categoryRepository.findAll()
        val response: MutableMap<String, Map<String, Any>> = mutableMapOf()
        var totalPrice = 0.0

        categories.forEach { category ->
            val cacheKey = "category:${category.id}:price:lowest"
            val cachedValue = cacheManager.getCache("priceCache")?.get(cacheKey)?.get() as? Pair<String, Double>

            val lowestPriceProduct = cachedValue ?: fetchAndCacheLowestPriceProduct(category, cacheKey)

            lowestPriceProduct.let { (brand, price) ->
                response[category.name] = mapOf(
                    "brand" to brand,
                    "price" to price
                )
                totalPrice += price
            }
        }

        response["총액"] = mapOf("totalPrice" to totalPrice)
        return response
    }

    // 구현2에 사용 함수
    @Transactional(readOnly = true)
    fun getLowestPriceBrandCacheData(): Map<String, Any> {
        val priceCache = cacheManager.getCache("priceCache")
        val cacheData = priceCache?.get(BRAND_CATEGORY_LOWEST_PRICE_KEY)?.get()
        if(cacheData == null){
            val brandPrice = getLowestPriceByBrandCategory()
            priceCache?.put(BRAND_CATEGORY_LOWEST_PRICE_KEY, brandPrice)
            priceCache?.put(CACHE_EXPIRATION_KEY, getCurrentTime().plusSeconds(20 * 60))
            return brandPrice
        } else {
            if(shouldRefreshCache()){
                val expirationTime = priceCache.get(CACHE_EXPIRATION_KEY)?.get() as? Instant
                if (expirationTime != null && !isCacheExpired(expirationTime) && Duration.between(getCurrentTime(), expirationTime).toMinutes() < CACHE_EXPIRATION_MINUTES) {
                    val result = getLowestPriceByBrandCategory()
                    priceCache.put(BRAND_CATEGORY_LOWEST_PRICE_KEY, result)
                    priceCache.put(CACHE_EXPIRATION_KEY, getCurrentTime().plusSeconds(20 * 60))
                }
            }
            return cacheData as Map<String, Any>
        }
    }

    // 구현3에 사용
    @Transactional(readOnly = true)
    fun getPriceRangeByCategory(categoryName: String): Map<String, Any> {
        val category = categoryRepository.findByName(categoryName)
            ?: throw CategoryNotFoundException("카테고리 '$categoryName'를 찾을 수 없습니다.")

        val lowestCacheKey = "category:${category.id}:price:lowest"
        val lowestCachedValue = cacheManager.getCache("priceCache")?.get(lowestCacheKey)?.get() as? Pair<String, Double>
        val lowestPriceProduct = lowestCachedValue ?: fetchAndCacheLowestPriceProduct(category)

        val highestCacheKey = "category:${category.id}:price:highest"
        val highestCachedValue = cacheManager.getCache("priceCache")?.get(highestCacheKey)?.get() as? Pair<String, Double>
        val highestPriceProduct = highestCachedValue ?: fetchAndCacheHighestPriceProduct(category)

        return mapOf(
            "카테고리" to category.name,
            "최저가" to listOf(mapOf("브랜드" to lowestPriceProduct.first, "가격" to lowestPriceProduct.second)),
            "최고가" to listOf(mapOf("브랜드" to highestPriceProduct.first, "가격" to highestPriceProduct.second))
        )
    }

    @Transactional(readOnly = true)
    suspend fun getAllProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            productRepository.findAll().map { ModelMapper.toDomain(it) }
        }
    }

    @Transactional
    suspend fun createProduct(productDTO: ProductDTO): Product {
        val productEntity = createProductEntity(productDTO)
        val savedProduct = withContext(Dispatchers.IO) {
            productRepository.save(productEntity)
        }
        updateCachePriceInfo(productDTO, "CREATE")
        return ModelMapper.toDomain(savedProduct)
    }

    @Transactional
    suspend fun updateProduct(productDTO: ProductDTO): Product {
        val existingProduct = findExistingProduct(productDTO)
        existingProduct.price = productDTO.price
        val updatedProduct = withContext(Dispatchers.IO) {
            productRepository.save(existingProduct)
        }
        updateCachePriceInfo(productDTO, "UPDATE")
        return ModelMapper.toDomain(updatedProduct)
    }

    @Transactional
    suspend fun deleteProduct(id: Long) {
        val existingProduct = withContext(Dispatchers.IO) {
            productRepository.findById(id)
        }
            .orElseThrow { ProductNotFoundException("삭제대상 상품을 찾을 수 없습니다. $id ") }
        withContext(Dispatchers.IO) {
            productRepository.deleteById(id)
        }

        updateCachePriceInfo(ProductDTO(existingProduct.brand.name, existingProduct.category.id, existingProduct.price), "DELETE")
    }

    @Transactional(readOnly = true)
    fun getLowestPriceByBrandCategory(): Map<String, Any> {
        val lowestPrices = productRepository.findLowestPricesByBrandAndCategory()
        val brandPriceMap = mutableMapOf<String, MutableList<Map<String, Any>>>()
        val totalPrices = mutableMapOf<String, Double>()

        lowestPrices.forEach { priceInfo ->
            val brandName = priceInfo.brandName
            val categoryName = priceInfo.categoryName
            val minPrice = priceInfo.minPrice

            brandPriceMap.getOrPut(brandName) { mutableListOf() }.add(
                mapOf("카테고리" to categoryName, "가격" to minPrice)
            )

            totalPrices[brandName] = totalPrices.getOrDefault(brandName, 0.0) + minPrice
        }

        val lowestBrand = totalPrices.minByOrNull { it.value }

        return lowestBrand?.let {
            val brandName = it.key
            mapOf(
                "최저가" to mapOf(
                    "브랜드" to brandName,
                    "카테고리" to brandPriceMap[brandName],
                    "총액" to it.value
                )
            )
        } ?: throw ProductNotFoundException("상품이 존재하지 않습니다.")
    }

    private fun getCurrentTime(): Instant = Instant.now()

    private fun isCacheExpired(expirationTime: Instant): Boolean {
        return Duration.between(getCurrentTime(), expirationTime).toMinutes() < CACHE_EXPIRATION_MINUTES
    }

    private fun shouldRefreshCache(): Boolean {
        return Math.random() < CACHE_REFRESH_CHANCE
    }


    private fun fetchAndCacheLowestPriceProduct(category: CategoryEntity, cacheKey: String): Pair<String, Double> {
        val lowestPriceProduct = productRepository.findTopByCategoryIdOrderByPriceAsc(category.id)

        return if (lowestPriceProduct != null) {
            val cacheValue: Pair<String, Double> = Pair(lowestPriceProduct.brand.name, lowestPriceProduct.price)
            cacheManager.getCache("priceCache")?.put(cacheKey, cacheValue)
            cacheValue
        } else {
            throw ProductNotFoundException("해당 카테고리(${category.name}) 상품 가격정보를 찾을 수 없습니다.")
        }
    }

    private fun fetchAndCacheLowestPriceProduct(category: CategoryEntity): Pair<String, Double> {
        val lowestPriceProduct = productRepository.findTopByCategoryIdOrderByPriceAsc(category.id)
            ?: throw ProductNotFoundException("해당 카테고리(${category.name}) 최저가 상품 정보를 찾을 수 없습니다.")
        return Pair(lowestPriceProduct.brand.name, lowestPriceProduct.price)
    }

    private fun fetchAndCacheHighestPriceProduct(category: CategoryEntity): Pair<String, Double> {
        val highestPriceProduct = productRepository.findTopByCategoryIdOrderByPriceDesc(category.id)
            ?: throw ProductNotFoundException("해당 카테고리(${category.name}) 최고가 상품 정보를 찾을 수 없습니다.")
        return Pair(highestPriceProduct.brand.name, highestPriceProduct.price)
    }

    private fun updateCachePriceInfo(productDTO: ProductDTO, operation: String) {
        coroutineScope.launch {
            val cacheKeyLowest = String.format(CACHE_KEY_LOWEST, productDTO.categoryId)
            val cacheKeyHighest = String.format(CACHE_KEY_HIGHEST, productDTO.categoryId)

            val cachedLowestValue = cacheManager.getCache("priceCache")?.get(cacheKeyLowest)?.get() as? Pair<String, Double>
            val cachedHighestValue = cacheManager.getCache("priceCache")?.get(cacheKeyHighest)?.get() as? Pair<String, Double>

            when (operation) {
                "CREATE", "UPDATE" -> updateCacheOnCreateOrUpdate(productDTO, cachedLowestValue, cachedHighestValue)
                "DELETE" -> updateCacheOnDelete(productDTO, cachedLowestValue, cachedHighestValue)
            }
        }
    }

    private fun updateCacheOnCreateOrUpdate(productDTO: ProductDTO, cachedLowestValue: Pair<String, Double>?, cachedHighestValue: Pair<String, Double>?) {
        cachedLowestValue?.let {
            if (productDTO.price < it.second) {
                cacheManager.getCache("priceCache")?.put(String.format(CACHE_KEY_LOWEST, productDTO.categoryId), Pair(productDTO.brandName, productDTO.price))
            }
        }

        cachedHighestValue?.let {
            if (productDTO.price > it.second) {
                cacheManager.getCache("priceCache")?.put(String.format(CACHE_KEY_HIGHEST, productDTO.categoryId), Pair(productDTO.brandName, productDTO.price))
            }
        }
    }

    private fun updateCacheOnDelete(productDTO: ProductDTO, cachedLowestValue: Pair<String, Double>?, cachedHighestValue: Pair<String, Double>?) {
        cachedLowestValue?.let {
            if (productDTO.price < it.second) {
                updateLowestCacheData(productDTO.categoryId)
            }
        }

        cachedHighestValue?.let {
            if (productDTO.price > it.second) {
                updateHighestCacheData(productDTO.categoryId)
            }
        }
    }

    private fun updateLowestCacheData(categoryId: Long) {
        productRepository.findTopByCategoryIdOrderByPriceAsc(categoryId)?.let { newLowestProduct ->
            cacheManager.getCache("priceCache")?.put(String.format(CACHE_KEY_LOWEST, categoryId), Pair(newLowestProduct.brand.name, newLowestProduct.price))
        } ?: cacheManager.getCache("priceCache")?.evict(String.format(CACHE_KEY_LOWEST, categoryId))
    }

    private fun updateHighestCacheData(categoryId: Long) {
        productRepository.findTopByCategoryIdOrderByPriceDesc(categoryId)?.let { newHighestProduct ->
            cacheManager.getCache("priceCache")?.put(String.format(CACHE_KEY_HIGHEST, categoryId), Pair(newHighestProduct.brand.name, newHighestProduct.price))
        } ?: cacheManager.getCache("priceCache")?.evict(String.format(CACHE_KEY_HIGHEST, categoryId))
    }

    private suspend fun findExistingProduct(productDTO: ProductDTO): ProductEntity {
        return withContext(Dispatchers.IO) {
            productRepository.findByBrandNameAndCategoryId(
                productDTO.brandName,
                productDTO.categoryId
            ) ?: throw ProductNotFoundException("${productDTO.brandName}, ${productDTO.categoryId}에 해당되는 상품을 찾을 수 없습니다.")
        }
    }

    private fun createProductEntity(productDTO: ProductDTO): ProductEntity {
        val brand = BrandEntity(productDTO.brandName)
        val category = findCategoryById(productDTO.categoryId)
        return ProductEntity(price = productDTO.price, brand = brand, category = category)
    }

    private fun findCategoryById(categoryId: Long): CategoryEntity {
        return categoryRepository.findById(categoryId)
            .orElseThrow { CategoryNotFoundException("카테고리 ID -  $categoryId 를 찾을 수 없습니다") }
    }
}
