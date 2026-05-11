package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.entities.supplier.SupplierEntity
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupplierJpaAdapter(
    private val repository: SupplierRepository
) : SupplierRepositoryPort {

    override fun save(supplier: SupplierEntity): SupplierEntity {
        return repository.save(supplier)
    }

    override fun findById(id: Long): SupplierEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<SupplierEntity> {
        return repository.findAll()
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}