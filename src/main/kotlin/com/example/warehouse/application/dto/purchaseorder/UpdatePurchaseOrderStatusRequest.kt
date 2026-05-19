package com.example.warehouse.application.dto.purchaseorder

import jakarta.validation.constraints.NotBlank

data class UpdatePurchaseOrderStatusRequest(
    @field:NotBlank
    val status: String
)