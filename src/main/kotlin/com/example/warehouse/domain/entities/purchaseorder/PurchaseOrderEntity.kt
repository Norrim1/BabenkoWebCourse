package com.example.warehouse.domain.entities.purchaseorder

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.purchaseorderitem.PurchaseOrderItemEntity
import com.example.warehouse.domain.entities.supplier.SupplierEntity
import com.example.warehouse.domain.enums.PurchaseOrderStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "purchase_orders")
class PurchaseOrderEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    val supplier: SupplierEntity,

    @Enumerated(EnumType.STRING)
    var status: PurchaseOrderStatus = PurchaseOrderStatus.CREATED,

    @OneToMany(mappedBy = "purchaseOrder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<PurchaseOrderItemEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date()
) {
    constructor() : this(null, SupplierEntity(),PurchaseOrderStatus.CREATED,mutableListOf(),Date())

    fun addItem(product: ProductEntity, quantity: Int) {
        require(quantity > 0)
        items.add(PurchaseOrderItemEntity(null,product = product, quantity = quantity, purchaseOrder = this))
    }
}
