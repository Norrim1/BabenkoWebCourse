package com.example.warehouse.application.dto.purchaseorder

import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemRequest
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class CreatePurchaseOrderRequest(
    @field:NotBlank
    val supplierId: Long,
    @field:NotEmpty
    val items: List<PurchaseOrderItemRequest>
)