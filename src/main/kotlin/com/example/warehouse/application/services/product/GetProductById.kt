package com.example.warehouse.application.services.product

import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class GetProductById(
    private val repository: ProductRepositoryPort
) {
    fun execute(id: Long): ProductDto {

        val product = repository.findById(id)
            ?: throw NotFoundException("Movement not found")

        return ProductDto(
            id = product.id,
            name = product.name
        )
    }
}