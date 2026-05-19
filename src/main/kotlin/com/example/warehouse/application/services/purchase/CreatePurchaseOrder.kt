package com.example.warehouse.application.services.purchase

import com.example.warehouse.application.dto.purchaseorder.CreatePurchaseOrderRequest
import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorderitem.PurchaseOrderItemDto
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.application.ports.PurchaseOrderRepositoryPort
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity
import com.example.warehouse.domain.exceptions.NotFoundException
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.util.Date

@Service
class CreatePurchaseOrder(
    private val supplierRepository: SupplierRepositoryPort,
    private val productRepository: ProductRepositoryPort,
    private val purchaseOrderRepository: PurchaseOrderRepositoryPort
) {

    @Transactional
    fun execute(@Valid @RequestBody request: CreatePurchaseOrderRequest): PurchaseOrderDto {

        val supplier = supplierRepository.findById(request.supplierId)
            ?: throw NotFoundException("Supplier not found")

        val order = PurchaseOrderEntity(null, supplier)

        request.items.forEach {
            val product = productRepository.findById(it.productId)
                ?: throw NotFoundException("Product not found")

            order.addItem(product, it.quantity)
        }

        val saved = purchaseOrderRepository.save(order)

        return PurchaseOrderDto(
            id = saved.id,
            supplierId = saved.supplier.id,
            supplierName = order.supplier.name,
            status = saved.status.name,
            createdAt = Date(),
            items = saved.items.map {
                PurchaseOrderItemDto(
                    productId = it.product.id,
                    productName = it.product.name,
                    quantity = it.quantity
                )
            }
        )
    }
}