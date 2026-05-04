package com.example.warehouse.application.dto.purchaseorder

import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class PurchaseOrderDto(
    @field:NotNull
    val id: Long?,
    @field:NotNull
    val supplierId: Long?,
    @field:NotBlank
    val status: String,
    @field:NotEmpty
    val items: List<PurchaseOrderItemDto>
)