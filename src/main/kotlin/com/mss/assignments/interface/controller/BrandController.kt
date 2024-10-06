package com.mss.assignments.`interface`.controller

import com.mss.assignments.application.service.BrandService
import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/brands")
class BrandController(private val brandService: BrandService) {

    @PostMapping
    fun createBrand(@RequestBody brand: String): ResponseEntity<BrandEntity> {
        val createdCategory = brandService.createBrand(brand)
        return ResponseEntity.ok(createdCategory)
    }
}
