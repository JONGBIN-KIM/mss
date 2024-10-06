package com.mss.assignments.domain.repository

import com.mss.assignments.application.dto.LowestPriceDto
import com.mss.assignments.infrastructure.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
}
