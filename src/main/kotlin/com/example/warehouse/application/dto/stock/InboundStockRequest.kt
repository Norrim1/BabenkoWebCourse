package com.example.warehouse.application.dto.stock

import jakarta.validation.constraints.*

data class InboundStockRequest(
    @field:NotNull
    val productId: Long,
    @field:NotNull
    val warehouseId: Long,
    @field:Positive
    val quantity: Int,
    @field:NotBlank
    val reason: String
)
