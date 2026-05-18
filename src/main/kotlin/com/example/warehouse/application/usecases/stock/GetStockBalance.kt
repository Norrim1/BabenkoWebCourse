package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import org.springframework.stereotype.Service

@Service
class GetStockBalance(
    private val stockBalanceRepository: StockBalanceRepositoryPort
) {

    fun execute(warehouseId: Long): List<StockBalanceDto> {
        return stockBalanceRepository
            .findAllByWarehouseId(warehouseId)
            .map {
                StockBalanceDto(
                    warehouseId = it.warehouse.id,
                    productId = it.product.id,
                    productName = it.product.name,
                    quantity = it.getQuantity()
                )
            }
    }
}