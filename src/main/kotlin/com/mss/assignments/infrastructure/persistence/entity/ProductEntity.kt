package com.mss.assignments.infrastructure.persistence.entity

import com.mss.assignments.domain.model.Product
import jakarta.persistence.*

@Entity
@Table(name = "products")
class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var price: Double = 0.0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    var brand: BrandEntity = BrandEntity(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity = CategoryEntity()
) {
    constructor() : this(null, 0.0, BrandEntity(), CategoryEntity())
}