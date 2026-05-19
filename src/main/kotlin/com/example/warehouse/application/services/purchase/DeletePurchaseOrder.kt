package com.example.warehouse.application.services.purchase

import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class DeletePurchaseOrder (
    private val repository: PurchaseOrderRepositoryPort
) {
    fun execute(id: Long) {

    val order = repository.findById(id)
        ?: { NotFoundException("Purchase order not found") }

    repository.deleteById(id)
    }
}