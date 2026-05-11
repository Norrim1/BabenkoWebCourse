package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Service

@Service
class DeleteWarehouse(
    private val warehouseRepository: WarehouseRepositoryPort,
    private val stockBalanceRepository: StockBalanceRepositoryPort
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