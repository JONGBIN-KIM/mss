package com.mss.assignments.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Id
    var id: Long,

    @Column(nullable = false, unique = true)
    var name: String = ""
) {
    constructor() : this(0, "")
}