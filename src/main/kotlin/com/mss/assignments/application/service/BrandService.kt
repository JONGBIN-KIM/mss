package com.mss.assignments.application.service

import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import com.mss.assignments.infrastructure.persistence.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(private val brandRepository: BrandRepository) {

    @Transactional
    fun createBrand(brand: String): BrandEntity {
        val brandEntity = BrandEntity(name = brand)
        return brandRepository.save(brandEntity)
    }
}
