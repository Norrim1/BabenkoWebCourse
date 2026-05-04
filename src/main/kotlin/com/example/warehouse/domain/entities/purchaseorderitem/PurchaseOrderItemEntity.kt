package com.example.warehouse.domain.entities.purchaseorderitem

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "purchase_order_items")
class PurchaseOrderItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,

    @Column(nullable = false)
    val quantity: Int,

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    val purchaseOrder: PurchaseOrderEntity
) {
    constructor() : this(null, ProductEntity(), 0, PurchaseOrderEntity())
}
