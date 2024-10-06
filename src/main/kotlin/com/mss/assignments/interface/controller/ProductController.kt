package com.mss.assignments.`interface`.controller

import com.mss.assignments.application.dto.ProductDTO
import com.mss.assignments.application.service.ProductService
import com.mss.assignments.domain.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @Operation(summary = "구현1 - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API",
        description = "카테고리 별로 최저 가격 브랜드와 상품 가격을 조회하고, 총액을 반환합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = Map::class))]),
        ApiResponse(responseCode = "404", description = "카테고리 상품 가격 정보 없음"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @GetMapping("/lowest-price")
    fun getLowestPriceByCategory(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(productService.getLowestPriceProducts())
    }

    @Operation(summary = "구현2 - 단일 브랜드로 모든 카테고리 상품 최저가격 조회 API",
        description = "PER알고리즘에 의한 캐시데이터 반환. 단일 브랜드로 모든 카테고리의 상품을 구매할 때 최저 가격에 판매하는 브랜드와 카테고리의 상품 가격, 총액을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = Map::class))]),
        ApiResponse(responseCode = "404", description = "브랜드 상품 가격 정보 없음"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @GetMapping("/lowest-price-brand")
    fun getLowestPriceBrandForAllCategories(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(productService.getLowestPriceBrandCacheData())
    }

    @Operation(summary = "구현3 - 카테고리 이름으로 최저, 최고 가격 브랜드 조회 API",
        description = "카테고리 이름으로 최저 및 최고 가격 브랜드와 상품 가격을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = Map::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 카테고리명"),
        ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @GetMapping("/price-range")
    fun getPriceRangeByCategory(@RequestParam categoryName: String): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(productService.getPriceRangeByCategory(categoryName))
    }

    @Operation(summary = "전체 상품 목록 조회 API",
        description = "전체 상품 목록을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = List::class))]),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @GetMapping
    suspend fun getAllProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(productService.getAllProducts())
    }

    @Operation(summary = "구현4 - 상품 생성 API",
        description = "브랜드 및 상품을 추가하는 API입니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "상품 생성 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = Product::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 요청"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @PostMapping
    suspend fun createProduct(@RequestBody productDTO: ProductDTO): ResponseEntity<Product> {
        return ResponseEntity.ok(productService.createProduct(productDTO))
    }

    @Operation(summary = "구현4 - 상품 업데이트 API",
        description = "브랜드 및 상품을 업데이트하는 API입니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "상품 업데이트 성공",
            content = [io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = io.swagger.v3.oas.annotations.media.Schema(implementation = Product::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 요청"),
        ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @PutMapping
    suspend fun updateProduct(@RequestBody productDTO: ProductDTO): ResponseEntity<Product> {
        return ResponseEntity.ok(productService.updateProduct(productDTO))
    }

    @Operation(summary = "구현4 - 상품 삭제 API",
        description = "상품을 삭제하는 API입니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ])
    @DeleteMapping("/{id}")
    suspend fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
