package com.mss.assignments.infrastructure.persistence.repository

import com.mss.assignments.application.dto.LowestPriceDto
import com.mss.assignments.domain.model.Product
import com.mss.assignments.infrastructure.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {

    fun findTopByCategoryIdOrderByPriceAsc(categoryId: Long): ProductEntity?

    fun findTopByCategoryIdOrderByPriceDesc(categoryId: Long): ProductEntity?

    @Query("""
    SELECT new com.mss.assignments.application.dto.LowestPriceDto(p.brand.name, p.category.name, MIN(p.price))
    FROM ProductEntity p
    GROUP BY p.brand.name, p.category.name
    """)
    fun findLowestPricesByBrandAndCategory(): List<LowestPriceDto>

    fun findByBrandNameAndCategoryId(brandName: String, categoryId: Long): ProductEntity?

}
