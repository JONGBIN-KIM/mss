package com.mss.assignments.application.mapper

import com.mss.assignments.application.dto.ProductDTO
import com.mss.assignments.domain.model.Brand
import com.mss.assignments.domain.model.Category
import com.mss.assignments.domain.model.Product
import com.mss.assignments.infrastructure.persistence.entity.BrandEntity
import com.mss.assignments.infrastructure.persistence.entity.CategoryEntity
import com.mss.assignments.infrastructure.persistence.entity.ProductEntity

object ModelMapper {

    fun toDomain(brandEntity: BrandEntity): Brand {
        return Brand(name = brandEntity.name)
    }

    fun toEntity(brand: Brand): BrandEntity {
        return BrandEntity(name = brand.name)
    }

    fun toDomain(categoryEntity: CategoryEntity): Category {
        return Category(id = categoryEntity.id ?: 0, name = categoryEntity.name)
    }

    fun toEntity(category: Category): CategoryEntity {
        return CategoryEntity(id = category.id, name = category.name)
    }

    fun toDomain(productEntity: ProductEntity): Product {
        return Product(
            id = productEntity.id ?: 0,
            price = productEntity.price,
            brand = toDomain(productEntity.brand),
            category = toDomain(productEntity.category)
        )
    }

    fun toEntity(product: Product): ProductEntity {
        return ProductEntity(
            id = product.id,
            price = product.price,
            brand = toEntity(product.brand),
            category = toEntity(product.category)
        )
    }

    fun toDTO(productEntity: ProductEntity): ProductDTO {
        return ProductDTO(
            brandName = productEntity.brand.name,
            categoryId = productEntity.category.id ?: 0,
            price = productEntity.price
        )
    }

    fun toDTO(product: Product): ProductDTO {
        return ProductDTO(
            brandName = product.brand.name,
            categoryId = product.category.id,
            price = product.price
        )
    }
}
