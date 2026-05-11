package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity
import com.example.warehouse.infrastructure.repositories.PurchaseOrderRepository
import org.springframework.stereotype.Component

@Component
class PurchaseOrderJpaAdapter(
    private val repository: PurchaseOrderRepository
) : PurchaseOrderRepositoryPort {

    override fun save(
        order: PurchaseOrderEntity
    ): PurchaseOrderEntity {
        return repository.save(order)
    }

    override fun findById(id: Long): PurchaseOrderEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<PurchaseOrderEntity> {
        return repository.findAll()
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}