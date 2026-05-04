package com.example.warehouse.application.dto.stock

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TransferStockRequest(
    @field:NotNull
    val productId: Long,
    @field:NotNull
    val fromWarehouseId: Long,
    @field:NotNull
    val toWarehouseId: Long,
    @field:Positive
    val quantity: Int,
    @field:NotBlank
    val reason: String
)