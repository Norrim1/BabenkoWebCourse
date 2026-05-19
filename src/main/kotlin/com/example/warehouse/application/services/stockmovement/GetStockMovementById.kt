package com.example.warehouse.application.services.stockmovement

import com.example.warehouse.application.dto.stockmovement.StockMovementDto
import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class GetStockMovementById(
    private val repository: StockMovementRepositoryPort
) {

    fun execute(id: Long): StockMovementDto {

        val movement = repository.findById(id)
            ?: throw NotFoundException("Movement not found")

        return StockMovementDto(
            id = movement.id,
            productId = movement.product.id!!,
            fromWarehouseId = movement.fromWarehouse?.id,
            toWarehouseId = movement.toWarehouse?.id,
            quantity = movement.quantity,
            type = movement.type.name,
            reason = movement.reason,
            createdBy = movement.createdBy.email,
            createdAt = movement.createdAt
        )
    }
}