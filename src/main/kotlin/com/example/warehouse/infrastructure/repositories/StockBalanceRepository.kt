package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.stockbalance.StockBalanceEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StockBalanceRepository : JpaRepository <StockBalanceEntity, Long> {
    fun findAllByWarehouseId(id: Long): List<StockBalanceEntity>
    fun findByProductIdAndWarehouseId(productId: Long, warehouseId: Long): StockBalanceEntity?
}