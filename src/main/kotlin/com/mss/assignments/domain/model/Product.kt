package com.mss.assignments.domain.model

data class Product(
    val id: Long,
    var price: Double,
    val brand: Brand,
    val category: Category
) {
    fun getProductName(): String {
        return "${brand.name} - ${category.name}"
    }
}