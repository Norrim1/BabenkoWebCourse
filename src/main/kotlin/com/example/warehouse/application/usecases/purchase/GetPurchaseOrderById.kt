package com.example.warehouse.application.usecases.purchase

import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrder
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class GetPurchaseOrderById (
    private val repository: PurchaseOrderRepositoryPort
) {
    fun execute(id: Long): PurchaseOrderDto {

        val order = repository.findById(id)
            ?: throw NotFoundException("Purchase Order not found")

        return PurchaseOrderDto(
            id = order.id,
            supplierId = order.supplier.id,
            status = order.status.name,
            items = order.items.map {
                PurchaseOrderItemDto(
                    productId = it.product.id,
                    quantity = it.quantity
                )
            }
        )
    }
}