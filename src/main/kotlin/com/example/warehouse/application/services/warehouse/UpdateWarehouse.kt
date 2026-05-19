package com.example.warehouse.application.services.warehouse

import com.example.warehouse.application.dto.warehouse.UpdateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class UpdateWarehouse(
    private val warehouseRepository: WarehouseRepositoryPort
) {

    fun execute(id: Long, request: UpdateWarehouseRequest): WarehouseDto {
        val warehouse = warehouseRepository.findById(id)
            ?: throw NotFoundException("Warehouse not found")

        warehouse.name = request.name

        val saved = warehouseRepository.save(warehouse)

        return WarehouseDto(saved.id, saved.name)
    }
}