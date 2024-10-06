package com.mss.assignments.infrastructure.persistence.repository

import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun findByName(name: String): CategoryEntity?
}
