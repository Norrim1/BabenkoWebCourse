package com.example.warehouse.application.usecases.purchase

import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.infrastructure.repositories.PurchaseOrderRepository
import org.springframework.stereotype.Service

@Service
class GetPurchaseOrders(
    private val purchaseOrderRepository: PurchaseOrderRepositoryPort
) {

    fun execute(): List<PurchaseOrderDto> {
        return purchaseOrderRepository.findAll().map { order ->
            PurchaseOrderDto(
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
}