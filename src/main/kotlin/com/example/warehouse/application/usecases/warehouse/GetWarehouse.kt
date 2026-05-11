package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Service

@Service
class GetWarehouse(
    private val warehouseRepository: WarehouseRepositoryPort
) {

    fun execute(): List<WarehouseDto> {
        return warehouseRepository.findAll().map {
            WarehouseDto(it.id, it.name)
        }
    }
}