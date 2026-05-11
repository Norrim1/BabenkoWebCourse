package com.example.warehouse.domain.entities.purchaseorder

import com.example.warehouse.domain.entities.purchaseorderitem.PurchaseOrderItemEntity
import com.example.warehouse.domain.entities.supplier.SupplierEntity
import com.example.warehouse.domain.enums.PurchaseOrderStatus

data class PurchaseOrder(
    val id: Long,
    val supplier: SupplierEntity,
    var status: PurchaseOrderStatus = PurchaseOrderStatus.CREATED,
    val items: MutableList<PurchaseOrderItemEntity> = mutableListOf()
)
