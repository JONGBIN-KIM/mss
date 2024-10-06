package com.mss.assignments.application.service

import com.mss.assignments.application.dto.CategoryDTO
import com.mss.assignments.application.dto.ProductDTO
import com.mss.assignments.application.exception.CategoryNotFoundException
import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import com.mss.assignments.infrastructure.persistence.entity.ProductEntity
import com.mss.assignments.infrastructure.persistence.repository.BrandRepository
import com.mss.assignments.infrastructure.persistence.repository.CategoryRepository
import com.mss.assignments.infrastructure.persistence.repository.ProductRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc
class InitialDataSetupTest @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productService: ProductService,
    private val productRepository: ProductRepository,
    private val mockMvc: MockMvc
) {

    private val categories = listOf(
        CategoryDTO(id = 1, name = "상의"),
        CategoryDTO(id = 2, name = "아우터"),
        CategoryDTO(id = 3, name = "바지"),
        CategoryDTO(id = 4, name = "스니커즈"),
        CategoryDTO(id = 5, name = "가방"),
        CategoryDTO(id = 6, name = "모자"),
        CategoryDTO(id = 7, name = "양말"),
        CategoryDTO(id = 8, name = "액세서리")
    )

    private val brands = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I")

    private val products = listOf(
        ProductDTO(brandName = "A", categoryId = 1, price = 11200.0),
        ProductDTO(brandName = "A", categoryId = 2, price = 5500.0),
        ProductDTO(brandName = "A", categoryId = 3, price = 4200.0),
        ProductDTO(brandName = "A", categoryId = 4, price = 9000.0),
        ProductDTO(brandName = "A", categoryId = 5, price = 2000.0),
        ProductDTO(brandName = "A", categoryId = 6, price = 1700.0),
        ProductDTO(brandName = "A", categoryId = 7, price = 1800.0),
        ProductDTO(brandName = "A", categoryId = 8, price = 2300.0),
        ProductDTO(brandName = "B", categoryId = 1, price = 10500.0),
        ProductDTO(brandName = "B", categoryId = 2, price = 5900.0),
        ProductDTO(brandName = "B", categoryId = 3, price = 3800.0),
        ProductDTO(brandName = "B", categoryId = 4, price = 9100.0),
        ProductDTO(brandName = "B", categoryId = 5, price = 2100.0),
        ProductDTO(brandName = "B", categoryId = 6, price = 2000.0),
        ProductDTO(brandName = "B", categoryId = 7, price = 2000.0),
        ProductDTO(brandName = "B", categoryId = 8, price = 2200.0),
        ProductDTO(brandName = "C", categoryId = 1, price = 10000.0),
        ProductDTO(brandName = "C", categoryId = 2, price = 6200.0),
        ProductDTO(brandName = "C", categoryId = 3, price = 3300.0),
        ProductDTO(brandName = "C", categoryId = 4, price = 9200.0),
        ProductDTO(brandName = "C", categoryId = 5, price = 2200.0),
        ProductDTO(brandName = "C", categoryId = 6, price = 1900.0),
        ProductDTO(brandName = "C", categoryId = 7, price = 2200.0),
        ProductDTO(brandName = "C", categoryId = 8, price = 2100.0),
        ProductDTO(brandName = "D", categoryId = 1, price = 10100.0),
        ProductDTO(brandName = "D", categoryId = 2, price = 5100.0),
        ProductDTO(brandName = "D", categoryId = 3, price = 3000.0),
        ProductDTO(brandName = "D", categoryId = 4, price = 9500.0),
        ProductDTO(brandName = "D", categoryId = 5, price = 2500.0),
        ProductDTO(brandName = "D", categoryId = 6, price = 1500.0),
        ProductDTO(brandName = "D", categoryId = 7, price = 2400.0),
        ProductDTO(brandName = "D", categoryId = 8, price = 2000.0),
        ProductDTO(brandName = "E", categoryId = 1, price = 10700.0),
        ProductDTO(brandName = "E", categoryId = 2, price = 5000.0),
        ProductDTO(brandName = "E", categoryId = 3, price = 3800.0),
        ProductDTO(brandName = "E", categoryId = 4, price = 9900.0),
        ProductDTO(brandName = "E", categoryId = 5, price = 2300.0),
        ProductDTO(brandName = "E", categoryId = 6, price = 1800.0),
        ProductDTO(brandName = "E", categoryId = 7, price = 2100.0),
        ProductDTO(brandName = "E", categoryId = 8, price = 2100.0),
        ProductDTO(brandName = "F", categoryId = 1, price = 11200.0),
        ProductDTO(brandName = "F", categoryId = 2, price = 7200.0),
        ProductDTO(brandName = "F", categoryId = 3, price = 4000.0),
        ProductDTO(brandName = "F", categoryId = 4, price = 9300.0),
        ProductDTO(brandName = "F", categoryId = 5, price = 2100.0),
        ProductDTO(brandName = "F", categoryId = 6, price = 1600.0),
        ProductDTO(brandName = "F", categoryId = 7, price = 2300.0),
        ProductDTO(brandName = "F", categoryId = 8, price = 1900.0),
        ProductDTO(brandName = "G", categoryId = 1, price = 10500.0),
        ProductDTO(brandName = "G", categoryId = 2, price = 5800.0),
        ProductDTO(brandName = "G", categoryId = 3, price = 3900.0),
        ProductDTO(brandName = "G", categoryId = 4, price = 9000.0),
        ProductDTO(brandName = "G", categoryId = 5, price = 2200.0),
        ProductDTO(brandName = "G", categoryId = 6, price = 1700.0),
        ProductDTO(brandName = "G", categoryId = 7, price = 2100.0),
        ProductDTO(brandName = "G", categoryId = 8, price = 2000.0),
        ProductDTO(brandName = "H", categoryId = 1, price = 10800.0),
        ProductDTO(brandName = "H", categoryId = 2, price = 6300.0),
        ProductDTO(brandName = "H", categoryId = 3, price = 3100.0),
        ProductDTO(brandName = "H", categoryId = 4, price = 9700.0),
        ProductDTO(brandName = "H", categoryId = 5, price = 2100.0),
        ProductDTO(brandName = "H", categoryId = 6, price = 1600.0),
        ProductDTO(brandName = "H", categoryId = 7, price = 2000.0),
        ProductDTO(brandName = "H", categoryId = 8, price = 2000.0),
        ProductDTO(brandName = "I", categoryId = 1, price = 11400.0),
        ProductDTO(brandName = "I", categoryId = 2, price = 6700.0),
        ProductDTO(brandName = "I", categoryId = 3, price = 3200.0),
        ProductDTO(brandName = "I", categoryId = 4, price = 9500.0),
        ProductDTO(brandName = "I", categoryId = 5, price = 2400.0),
        ProductDTO(brandName = "I", categoryId = 6, price = 1700.0),
        ProductDTO(brandName = "I", categoryId = 7, price = 1700.0),
        ProductDTO(brandName = "I", categoryId = 8, price = 2400.0)
    )

    @BeforeEach
    fun setUp() {
        categories.forEach { category ->
            categoryRepository.save(CategoryEntity(id = category.id, name = category.name))
        }

        brands.forEach { brand ->
            brandRepository.save(BrandEntity(name = brand))
        }

        products.forEach { productDTO ->
            val brandEntity = brandRepository.findById(productDTO.brandName).orElseThrow {
                RuntimeException("Brand ${productDTO.brandName} not found")
            }
            val categoryEntity = categoryRepository.findById(productDTO.categoryId).orElseThrow {
                CategoryNotFoundException("Category ID ${productDTO.categoryId} not found")
            }
            productRepository.save(ProductEntity(price = productDTO.price, brand = brandEntity, category = categoryEntity))
        }
    }

    @Test
    fun `구현1에서 I 브랜드의 상의 가격을 2500원으로 변경하면 상의카테고리 최저가가 I로 변경된다`() = runBlocking {
        val productToUpdate = productRepository.findAll().find { it.brand.name == "I" && it.category.name == "상의" }
        assertNotNull(productToUpdate)

        productToUpdate.price = 2500.0
        productRepository.save(productToUpdate)

        val lowestPriceProducts = productService.getLowestPriceProducts()
        assertEquals("I", lowestPriceProducts["상의"]?.get("brand"))
    }

    @Test
    fun `구현2에서 카테고리의 모든상품합계가 다른 브랜드일때, I 브랜드의 상의 가격을 1000원으로 변경하면 가장낮은 브랜드가 I로 변경된다`() = runBlocking {
        val productToUpdate = productRepository.findAll().find { it.brand.name == "I" && it.category.name == "상의" }
        assertNotNull(productToUpdate)

        productToUpdate.price = 1000.0
        productRepository.save(productToUpdate)

        val lowestPriceBrandData = productService.getLowestPriceBrandCacheData() as Map<String, Any>
        val lowestBrand = lowestPriceBrandData["최저가"] as Map<String, Any>
        assertEquals("I", lowestBrand["브랜드"])
    }

    @Test
    fun `C 브랜드의 가방 가격을 3000원으로 변경해도 구현2의 캐시 데이터는 변하지 않는다`() = runBlocking {
        val initialCacheData = productService.getLowestPriceBrandCacheData()

        val updatedProduct = ProductDTO(brandName = "C", categoryId = 5, price = 3000.0)

        mockMvc.put("/products") {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            content = """{"brandName": "${updatedProduct.brandName}", "categoryId": ${updatedProduct.categoryId}, "price": ${updatedProduct.price}}"""
        }.andExpect {
            status { isOk() }
        }

        val updatedCacheData = productService.getLowestPriceBrandCacheData()

        val initialLowestBrand = initialCacheData["최저가"]
        val updatedLowestBrand = updatedCacheData["최저가"]

        assertEquals(initialLowestBrand, updatedLowestBrand)
    }


    @Test
    fun `구현4+구현1 에서 최저가인 상품을 삭제하면 카테고리 최저가가 변경된다`() = runBlocking {
        val productToDelete = productRepository.findAll().find { it.brand.name == "C" && it.category.name == "상의" }
        assertNotNull(productToDelete)

        productRepository.delete(productToDelete)

        val lowestPriceProducts = productService.getLowestPriceProducts()
        assertNotEquals("C", lowestPriceProducts["상의"]?.get("brand"))
    }

}
