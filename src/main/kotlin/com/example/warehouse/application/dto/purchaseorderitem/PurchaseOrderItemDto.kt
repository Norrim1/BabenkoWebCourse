package com.example.warehouse.application.dto.purchaseorderitem

data class PurchaseOrderItemDto(
    val productId: Long?,
    val productName: String,
    val quantity: Int
)