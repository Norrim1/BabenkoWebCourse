package com.example.warehouse.application.services.purchase

import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
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
                supplierName = order.supplier.name,
                status = order.status.name,
                createdAt = order.createdAt,
                items = order.items.map {
                    PurchaseOrderItemDto(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity
                    )
                }
            )
        }
    }
}