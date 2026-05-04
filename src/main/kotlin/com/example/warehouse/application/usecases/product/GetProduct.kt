package com.example.warehouse.application.usecases.product

import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.infrastructure.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class GetProduct(
    private val productRepository: ProductRepository
) {

    fun execute(): List<ProductDto> {
        return productRepository.findAll().map {
            ProductDto(it.id, it.name)
        }
    }
}