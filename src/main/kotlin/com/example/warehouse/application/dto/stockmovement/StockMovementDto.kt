package com.example.warehouse.application.dto.stockmovement

import java.util.Date

data class StockMovementDto(
    val id: Long?,
    val productId: Long,
    val fromWarehouseId: Long?,
    val toWarehouseId: Long?,
    val quantity: Int,
    val type: String,
    val reason: String,
    val createdBy: String,
    val createdAt: Date
)