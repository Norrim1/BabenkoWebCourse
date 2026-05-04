package com.example.warehouse.application.dto.supplier

import jakarta.validation.constraints.NotBlank

data class UpdateSupplierRequest(
    @field:NotBlank
    val name: String
)