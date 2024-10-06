package com.mss.assignments.infrastructure.persistence.repository

import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<BrandEntity, String> {
}
