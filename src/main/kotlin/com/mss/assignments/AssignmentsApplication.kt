package com.mss.assignments

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import io.swagger.v3.oas.annotations.OpenAPIDefinition

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition
class AssignmentsApplication

fun main(args: Array<String>) {
    runApplication<AssignmentsApplication>(*args)
}
