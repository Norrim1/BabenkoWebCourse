package com.example.warehouse.application.usecases.stock

import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetStockBalance(
    private val stockBalanceRepository: StockBalanceRepository
) {

    fun execute(warehouseId: UUID): List<StockBalanceDto> {
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