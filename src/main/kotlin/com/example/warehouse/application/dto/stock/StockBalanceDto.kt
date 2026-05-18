package com.example.warehouse.application.dto.stock

data class StockBalanceDto(
    val warehouseId: Long?,
    val productId: Long?,
    val productName: String,
    val quantity: Int
)