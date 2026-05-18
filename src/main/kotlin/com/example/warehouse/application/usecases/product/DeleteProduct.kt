package com.example.warehouse.application.usecases.product

import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.application.ports.StockBalanceRepositoryPort
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import com.example.warehouse.infrastructure.repositories.StockBalanceRepository
import org.springframework.stereotype.Service

@Service
class DeleteProduct(
    private val productRepository: ProductRepositoryPort,
    private val stockBalanceRepository: StockBalanceRepositoryPort
) {

    fun execute(id: Long) {
        val existsInStock = stockBalanceRepository.findAll()
            .any { it.product.id == id }

        productRepository.findById(id)
            ?: throw NotFoundException("Movement not found")

        if (existsInStock) {
            throw ConflictException("Cannot delete product with stock balances")
        }

        productRepository.deleteById(id)
    }
}