package com.example.warehouse.application.dto.product

import jakarta.validation.constraints.NotBlank

data class UpdateProductRequest(
    @field:NotBlank
    val name: String
)