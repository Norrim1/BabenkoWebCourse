package com.example.warehouse.application.services.stock

import com.example.warehouse.application.dto.stock.OutboundStockRequest
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.domain.enums.MovementType
import com.example.warehouse.domain.exceptions.NotFoundException
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class OutboundStock(
    private val productRepository: ProductRepositoryPort,
    private val warehouseRepository: WarehouseRepositoryPort,
    private val stockBalanceRepository: StockBalanceRepositoryPort,
    private val stockMovementRepository: StockMovementRepositoryPort,
) {

    @Transactional
    fun execute(@Valid @RequestBody request: OutboundStockRequest, userDetails: UserEntity) {
        val product = productRepository.findById(request.productId)
            ?: throw NotFoundException("Product not found")

        val warehouse = warehouseRepository.findById(request.warehouseId)
            ?: throw NotFoundException("Warehouse not found")

        val balance = stockBalanceRepository.getOrCreate(product, warehouse)

        balance.decrease(request.quantity)

        stockMovementRepository.save(
            StockMovementEntity(
                id = null,
                product = product,
                fromWarehouse = warehouse,
                toWarehouse = null,
                quantity = request.quantity,
                type = MovementType.OUTBOUND,
                reason = request.reason,
                createdBy = userDetails
            )
        )
    }
}