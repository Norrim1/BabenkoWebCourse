package com.example.warehouse.application.dto.product

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateProductRequest(
    @field:NotBlank
    @Size(max = 255)
    val name: String
)