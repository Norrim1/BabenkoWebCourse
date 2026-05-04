package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Service

@Service
class DeleteWarehouse(
    private val warehouseRepository: WarehouseRepository,
    private val stockBalanceRepository: StockBalanceRepository
) {

    fun execute(id: Long) {
        val hasStock = stockBalanceRepository.findAll()
            .any { it.warehouse.id == id }

        if (hasStock) {
            throw ConflictException("Cannot delete warehouse with stock")
        }

        warehouseRepository.deleteById(id)
    }
}