package com.example.warehouse.application.usecases.warehouse

import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class GetWarehouseById(
    private val repository: WarehouseRepositoryPort
) {
    fun execute(id: Long): WarehouseDto {

        val warehouse = repository.findById(id)
            ?: throw NotFoundException("Movement not found")

        return WarehouseDto(
            id = warehouse.id,
            name = warehouse.name
        )
    }
}