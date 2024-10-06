package com.mss.assignments.`interface`.controller

import com.mss.assignments.application.dto.CategoryDTO
import com.mss.assignments.application.service.CategoryService
import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping
    fun createCategory(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<CategoryEntity> {
        val createdCategory = categoryService.createCategory(categoryDTO)
        return ResponseEntity.ok(createdCategory)
    }

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryEntity>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(categories)
    }
}
