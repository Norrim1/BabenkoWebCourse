package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.application.dto.warehouse.UpdateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Service

@Service
class UpdateWarehouse(
    private val warehouseRepository: WarehouseRepository
) {

    fun execute(id: Long, request: UpdateWarehouseRequest): WarehouseDto {
        val warehouse = warehouseRepository.findById(id)
            .orElseThrow { RuntimeException("Warehouse not found") }

        warehouse.name = request.name

        val saved = warehouseRepository.save(warehouse)

        return WarehouseDto(saved.id, saved.name)
    }
}