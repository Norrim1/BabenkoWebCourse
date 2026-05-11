package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.infrastructure.repositories.StockMovementRepository
import org.springframework.stereotype.Component

@Component
class StockMovementJpaAdapter(
    private val repository: StockMovementRepository
) : StockMovementRepositoryPort {

    override fun save(
        movement: StockMovementEntity
    ): StockMovementEntity {
        return repository.save(movement)
    }

    override fun findById(id: Long): StockMovementEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<StockMovementEntity> {
        return repository.findAll()
    }
}