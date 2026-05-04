package com.example.warehouse.application.usecases.product

import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import org.springframework.stereotype.Service

@Service
class DeleteProduct(
    private val productRepository: ProductRepository,
    private val stockBalanceRepository: StockBalanceRepository
) {

    fun execute(id: Long) {
        val existsInStock = stockBalanceRepository.findAll()
            .any { it.product.id == id }

        if (existsInStock) {
            throw ConflictException("Cannot delete product with stock balances")
        }

        productRepository.deleteById(id)
    }
}