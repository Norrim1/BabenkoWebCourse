package com.example.warehouse.domain.entities.purchaseorderitem

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity

data class PurchaseOrderItem(
    val id: Long,
    val product: ProductEntity,
    val quantity: Int,
    val purchaseOrder: PurchaseOrderEntity
)
