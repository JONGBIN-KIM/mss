package com.mss.assignments.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "brands")
class BrandEntity(
    @Id
    @Column(nullable = false, unique = true)
    var name: String = ""
) {
    constructor() : this("")
}
