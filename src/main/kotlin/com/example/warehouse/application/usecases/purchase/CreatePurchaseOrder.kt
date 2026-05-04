package com.example.warehouse.application.usecases.purchase

import com.example.warehouse.application.dto.purchaseorder.CreatePurchaseOrderRequest
import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.PurchaseOrderRepository
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class CreatePurchaseOrder(
    private val supplierRepository: SupplierRepository,
    private val productRepository: ProductRepository,
    private val purchaseOrderRepository: PurchaseOrderRepository
) {

    @Transactional
    fun execute(@Valid @RequestBody request: CreatePurchaseOrderRequest): PurchaseOrderDto {

        val supplier = supplierRepository.findById(request.supplierId)
            .orElseThrow { NotFoundException("Supplier not found") }

        val order = PurchaseOrderEntity(null, supplier)

        request.items.forEach {
            val product = productRepository.findById(it.productId)
                .orElseThrow { NotFoundException("Product not found") }

            order.addItem(product, it.quantity)
        }

        val saved = purchaseOrderRepository.save(order)

        return PurchaseOrderDto(
            id = saved.id,
            supplierId = saved.supplier.id,
            status = saved.status.name,
            items = saved.items.map {
                PurchaseOrderItemDto(
                    productId = it.product.id,
                    quantity = it.quantity
                )
            }
        )
    }
}