package com.example.warehouse.application.services.stock

import com.example.warehouse.application.dto.stock.TransferStockRequest
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.domain.enums.MovementType
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.domain.exceptions.NotFoundException
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.util.Date

@Service
class TransferStock(
    private val productRepository: ProductRepositoryPort,
    private val warehouseRepository: WarehouseRepositoryPort,
    private val stockBalanceRepository: StockBalanceRepositoryPort,
    private val stockMovementRepository: StockMovementRepositoryPort
) {

    @Transactional
    fun execute(@Valid @RequestBody request: TransferStockRequest, userDetails: UserEntity) {
        if (request.fromWarehouseId == request.toWarehouseId) {
            throw ConflictException("Warehouses must be different")
        }

        val product = productRepository.findById(request.productId)
            ?: throw NotFoundException("Product not found")

        val fromWarehouse = warehouseRepository.findById(request.fromWarehouseId)
            ?: throw NotFoundException("From warehouse not found")

        val toWarehouse = warehouseRepository.findById(request.toWarehouseId)
            ?: throw NotFoundException("To warehouse not found")

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
                reason = request.reason,
                createdBy = userDetails,
                createdAt = Date()
            )
        )
    }
}