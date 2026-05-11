package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class WarehouseJpaAdapter(
    private val repository: WarehouseRepository
) : WarehouseRepositoryPort {

    override fun save(warehouse: WarehouseEntity): WarehouseEntity {
        return repository.save(warehouse)
    }

    override fun findById(id: Long): WarehouseEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<WarehouseEntity> {
        return repository.findAll()
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}