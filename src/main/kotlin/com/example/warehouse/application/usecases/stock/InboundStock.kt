package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.InboundStockRequest
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.domain.enums.MovementType
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import com.example.warehouse.infrastructure.repositories.StockMovementRepository
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody

@Service
class InboundStock(
    private val productRepository: ProductRepository,
    private val warehouseRepository: WarehouseRepository,
    private val stockBalanceRepository: StockBalanceRepository,
    private val stockMovementRepository: StockMovementRepository
) {

    @Transactional
    fun execute(@Valid @RequestBody request: InboundStockRequest) {
        val product = productRepository.findById(request.productId)
            .orElseThrow { NotFoundException("Product not found") }

        val warehouse = warehouseRepository.findById(request.warehouseId)
            .orElseThrow { NotFoundException("Warehouse not found") }

        val balance = stockBalanceRepository.getOrCreate(product, warehouse)

        balance.increase(request.quantity)

        stockMovementRepository.save(
            StockMovementEntity(
                id = null,
                product = product,
                fromWarehouse = null,
                toWarehouse = warehouse,
                quantity = request.quantity,
                type = MovementType.INBOUND,
                reason = request.reason
            )
        )
    }

    companion object
}