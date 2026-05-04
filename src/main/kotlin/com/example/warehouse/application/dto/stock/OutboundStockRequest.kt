package com.example.warehouse.application.dto.stock

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class OutboundStockRequest(
    @field:NotNull
    val productId: Long,
    @field:NotNull
    val warehouseId: Long,
    @field:Positive
    val quantity: Int,
    @field:NotBlank
    val reason: String
)