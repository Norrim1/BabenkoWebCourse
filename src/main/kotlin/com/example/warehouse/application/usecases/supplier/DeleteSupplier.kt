package com.example.warehouse.application.usecases.supplier

import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.infrastructure.repositories.PurchaseOrderRepository
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import org.springframework.stereotype.Service

@Service
class DeleteSupplier(
    private val supplierRepository: SupplierRepositoryPort,
    private val purchaseOrderRepository: PurchaseOrderRepository
) {

    fun execute(id: Long) {
        val existsInOrders = purchaseOrderRepository.findAll()
            .any { it.supplier.id == id }

        if (existsInOrders) {
            throw ConflictException("Cannot delete supplier with purchase orders")
        }

        supplierRepository.deleteById(id)
    }
}