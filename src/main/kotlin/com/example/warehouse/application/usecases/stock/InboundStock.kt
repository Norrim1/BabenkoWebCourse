package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.InboundStockRequest
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.application.ports.StockMovementRepositoryPort
import com.example.warehouse.application.ports.WarehouseRepositoryPort
import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.domain.enums.MovementType
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import com.example.warehouse.infrastructure.repositories.StockMovementRepository
import com.example.warehouse.infrastructure.repositories.UserRepository
import com.example.warehouse.infrastructure.repositories.WarehouseRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody

@Service
class InboundStock(
    private val productRepository: ProductRepositoryPort,
    private val warehouseRepository: WarehouseRepositoryPort,
    private val stockBalanceRepository: StockBalanceRepositoryPort,
    private val stockMovementRepository: StockMovementRepositoryPort,
) {

    @Transactional
    fun execute(@Valid @RequestBody request: InboundStockRequest, userDetails: UserEntity) {

        val product = productRepository.findById(request.productId)
            ?: throw NotFoundException("Product not found")

        val warehouse = warehouseRepository.findById(request.warehouseId)
            ?: throw NotFoundException("Warehouse not found")

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
                reason = request.reason,
                createdBy = userDetails
            )
        )
    }

    companion object
}