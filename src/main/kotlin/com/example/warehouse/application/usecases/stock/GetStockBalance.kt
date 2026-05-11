package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetStockBalance(
    private val stockBalanceRepository: StockBalanceRepositoryPort
) {

    fun execute(warehouseId: Long): List<StockBalanceDto> {
        return stockBalanceRepository
            .findAllByWarehouseId(warehouseId)
            .map {
                StockBalanceDto(
                    productId = it.product.id,
                    productName = it.product.name,
                    quantity = it.getQuantity()
                )
            }
    }
}