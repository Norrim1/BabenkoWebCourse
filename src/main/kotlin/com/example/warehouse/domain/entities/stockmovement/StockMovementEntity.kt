package com.example.warehouse.domain.entities.stockmovement

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.domain.enums.MovementType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "stock_movements")
class StockMovementEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,

    @ManyToOne
    @JoinColumn(name = "from_warehouse_id")
    val fromWarehouse: WarehouseEntity?,

    @ManyToOne
    @JoinColumn(name = "to_warehouse_id")
    val toWarehouse: WarehouseEntity?,

    @Column(nullable = false)
    val quantity: Int,

    @Enumerated(EnumType.STRING)
    val type: MovementType,

    @Column(nullable = false)
    val reason: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    val createdBy: UserEntity,

    @Column(nullable = false)
    val createdAt: Date = Date()
) {
    constructor() : this(null, ProductEntity(), null, null,0, MovementType.INBOUND, "", UserEntity(),Date())
}