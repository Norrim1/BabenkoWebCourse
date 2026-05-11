package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.application.dto.warehouse.CreateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Service

@Service
class CreateWarehouse(
    private val warehouseRepository: WarehouseRepositoryPort
) {

    fun execute(request: CreateWarehouseRequest): WarehouseDto {
        val warehouse = WarehouseEntity(null,request.name)
        val saved = warehouseRepository.save(warehouse)

        return WarehouseDto(
            id = saved.id,
            name = saved.name
        )
    }
}