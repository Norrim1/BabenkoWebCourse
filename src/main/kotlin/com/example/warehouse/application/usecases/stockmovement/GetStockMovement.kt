package com.example.warehouse.application.usecases.stockmovement

import com.example.warehouse.application.dto.stockmovement.StockMovementDto
import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.infrastructure.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class GetStockMovement(
    private val repository: StockMovementRepositoryPort,
    private val userRepository: UserRepository
) {

    fun execute(): List<StockMovementDto> {

        return repository.findAll().map {

            StockMovementDto(
                id = it.id,
                productId = it.product.id!!,
                fromWarehouseId = it.fromWarehouse?.id,
                toWarehouseId = it.toWarehouse?.id,
                quantity = it.quantity,
                type = it.type.name,
                reason = it.reason,
                createdBy = it.createdBy.email,
                createdAt = it.createdAt
            )
        }
    }
}