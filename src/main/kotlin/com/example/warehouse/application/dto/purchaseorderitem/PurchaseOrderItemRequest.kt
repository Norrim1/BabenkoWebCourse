package com.example.warehouse.application.dto.purchaseorderitem

import jakarta.validation.constraints.NotNull

data class PurchaseOrderItemRequest(
    @field:NotNull
    val productId: Long,
    @field:NotNull
    val quantity: Int
)