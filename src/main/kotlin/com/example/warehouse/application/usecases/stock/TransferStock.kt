package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.TransferStockRequest
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.domain.enums.MovementType
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import com.example.warehouse.infrastructure.repositories.StockMovementRepository
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class TransferStock(
    private val productRepository: ProductRepository,
    private val warehouseRepository: WarehouseRepository,
    private val stockBalanceRepository: StockBalanceRepository,
    private val stockMovementRepository: StockMovementRepository
) {

    @Transactional
    fun execute(@Valid @RequestBody request: TransferStockRequest) {
        if (request.fromWarehouseId == request.toWarehouseId) {
            throw ConflictException("Warehouses must be different")
        }

        val product = productRepository.findById(request.productId)
            .orElseThrow { NotFoundException("Product not found") }

        val fromWarehouse = warehouseRepository.findById(request.fromWarehouseId)
            .orElseThrow { NotFoundException("From warehouse not found") }

        val toWarehouse = warehouseRepository.findById(request.toWarehouseId)
            .orElseThrow { NotFoundException("To warehouse not found") }

        val fromBalance = stockBalanceRepository.getOrCreate(product, fromWarehouse)
        val toBalance = stockBalanceRepository.getOrCreate(product, toWarehouse)

        fromBalance.decrease(request.quantity)
        toBalance.increase(request.quantity)

        stockMovementRepository.save(
            StockMovementEntity(
                id = null,
                product = product,
                fromWarehouse = fromWarehouse,
                toWarehouse = toWarehouse,
                quantity = request.quantity,
                type = MovementType.TRANSFER,
                reason = request.reason
            )
        )
    }
}