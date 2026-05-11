package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.stockbalance.StockBalanceEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import org.springframework.stereotype.Component

@Component
class StockBalanceJpaAdapter(
    private val repository: StockBalanceRepository
) : StockBalanceRepositoryPort {

    override fun save(
        stockBalance: StockBalanceEntity
    ): StockBalanceEntity {
        return repository.save(stockBalance)
    }

    override fun findById(id: Long): StockBalanceEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<StockBalanceEntity> {
        return repository.findAll()
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    override fun findByProductIdAndWarehouseId(
        productId: Long,
        warehouseId: Long
    ): StockBalanceEntity? {
        return repository.findByProductIdAndWarehouseId(
            productId,
            warehouseId
        )
    }

    override fun findAllByWarehouseId(
        warehouseId: Long
    ): List<StockBalanceEntity> {
        return repository.findAllByWarehouseId(warehouseId)
    }

    override fun getOrCreate(product: ProductEntity, warehouse: WarehouseEntity): StockBalanceEntity {
        return findAll().find {
            it.product.id == product.id && it.warehouse.id == warehouse.id
        } ?: save(StockBalanceEntity(null, product = product, warehouse = warehouse))
    }
}