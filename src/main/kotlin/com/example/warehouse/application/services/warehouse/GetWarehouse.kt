package com.example.warehouse.application.services.warehouse

import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.WarehouseRepositoryPort
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