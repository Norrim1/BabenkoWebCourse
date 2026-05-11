package com.example.warehouse.domain.entities.stockmovement

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.domain.enums.MovementType
import java.util.Date

data class StockMovement(
    val id: Long?,
    val product: ProductEntity,
    val fromWarehouse: WarehouseEntity?,
    val toWarehouse: WarehouseEntity?,
    val quantity: Int,
    val type: MovementType,
    val reason: String,
    val createdAt: Date = Date()
)
