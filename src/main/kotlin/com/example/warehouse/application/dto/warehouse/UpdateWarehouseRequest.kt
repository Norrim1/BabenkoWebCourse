package com.example.warehouse.application.dto.warehouse

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateWarehouseRequest(
    @field:NotBlank
    @Size(max = 255)
    val name: String
)