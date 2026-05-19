package com.example.warehouse.application.dto.purchaseorder

import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.Date

data class PurchaseOrderDto(
    @field:NotNull
    val id: Long?,
    @field:NotNull
    val supplierId: Long?,
    @field:NotNull
    val supplierName: String,
    @field:NotBlank
    val status: String,
    @field:NotEmpty
    val items: List<PurchaseOrderItemDto>,
    @field:NotEmpty
    val createdAt: Date
)