package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.product.Product
import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.stockbalance.StockBalance
import com.example.warehouse.domain.entities.stockbalance.StockBalanceEntity
import com.example.warehouse.domain.entities.warehouse.Warehouse
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StockBalanceRepository : JpaRepository <StockBalanceEntity, Long> {
    fun findAllByWarehouseId(id: UUID): List<StockBalanceEntity>
    fun getOrCreate(product: ProductEntity, warehouse: WarehouseEntity): StockBalanceEntity {
        return findAll().find {
            it.product.id == product.id && it.warehouse.id == warehouse.id
        } ?: save(StockBalanceEntity(null, product = product, warehouse = warehouse))
    }
}