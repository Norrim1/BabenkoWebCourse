package com.example.warehouse.application.dto.supplier

import jakarta.validation.constraints.NotBlank

data class CreateSupplierRequest(
    @field:NotBlank
    val name: String
)