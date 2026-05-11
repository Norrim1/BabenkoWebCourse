package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.stockbalance.StockBalanceEntity
import com.example.warehouse.domain.entities.warehouse.Warehouse
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity

interface StockBalanceRepositoryPort {

    fun save(stockBalance: StockBalanceEntity): StockBalanceEntity

    fun findById(id: Long): StockBalanceEntity?

    fun findAll(): List<StockBalanceEntity>

    fun deleteById(id: Long)

    fun findByProductIdAndWarehouseId(
        productId: Long,
        warehouseId: Long
    ): StockBalanceEntity?

    fun findAllByWarehouseId(warehouseId: Long): List<StockBalanceEntity>

    fun getOrCreate(product: ProductEntity, warehouse: WarehouseEntity): StockBalanceEntity
}