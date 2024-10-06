package com.mss.assignments.`interface`.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ProductViewController() {

    @GetMapping("/musinsa/recommend")
    fun initializeDataView(model: Model): String {
        return "musinsa/recommend"
    }
}
