package com.mss.assignments.application.service

import com.mss.assignments.application.dto.CategoryDTO
import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import com.mss.assignments.infrastructure.persistence.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    @Transactional
    fun createCategory(categoryDTO: CategoryDTO): CategoryEntity {
        val categoryEntity = CategoryEntity(id = categoryDTO.id, name = categoryDTO.name)
        return categoryRepository.save(categoryEntity)
    }

    @Transactional(readOnly = true)
    fun getAllCategories(): List<CategoryEntity> {
        return categoryRepository.findAll()
    }
}
