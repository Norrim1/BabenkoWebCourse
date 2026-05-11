package com.example.warehouse.domain.entities.stockbalance

import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import com.example.warehouse.domain.exceptions.BadRequestException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.persistence.Version
import java.util.UUID

@Entity
@Table(
    name = "stock_balances",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["product_id", "warehouse_id"])
    ]
)
open class StockBalanceEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    val warehouse: WarehouseEntity,

    @Column(nullable = false)
    private var quantity: Int = 0
) {
    constructor() : this(null, ProductEntity(), WarehouseEntity(),0)

    fun increase(amount: Int) {
        require(amount > 0) { "Amount must be positive" }
        quantity += amount
    }

    fun decrease(amount: Int) {
        require(amount > 0) { "Amount must be positive" }
        if (quantity - amount < 0) {
            throw BadRequestException("Stock cannot be negative")
        }
        quantity -= amount
    }

    fun getQuantity(): Int = quantity
}