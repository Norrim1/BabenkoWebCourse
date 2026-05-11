package com.example.warehouse.domain.entities.stockbalance

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity

data class StockBalance(
    val id: Long?,
    val product: ProductEntity,
    val warehouse: WarehouseEntity,
    private var quantity: Int = 0
)
