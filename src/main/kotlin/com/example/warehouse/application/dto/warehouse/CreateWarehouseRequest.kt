package com.example.warehouse.application.dto.warehouse

import jakarta.validation.constraints.NotBlank

data class CreateWarehouseRequest(
    @field:NotBlank
    val name: String
)
