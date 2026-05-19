package com.example.warehouse.application.services.purchase

import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorder.UpdatePurchaseOrderStatusRequest
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.domain.enums.PurchaseOrderStatus
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class UpdatePurchaseOrder(
    private val repository: PurchaseOrderRepositoryPort
) {
    fun execute(
        id: Long,
        request: UpdatePurchaseOrderStatusRequest
    ): PurchaseOrderDto {

        val order =
            repository.findById(id) ?: throw NotFoundException("Purchase Order not found")

        val status = runCatching { PurchaseOrderStatus.valueOf(request.status.uppercase()) }.getOrElse {
            throw NotFoundException("Purchase Order status not found")
        }

        order.status = status
        val saved = repository.save(order)

        return PurchaseOrderDto(
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