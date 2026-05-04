package com.example.warehouse.application.usecases.product

import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.application.dto.product.UpdateProductRequest
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.ProductRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class UpdateProduct(
    private val productRepository: ProductRepository
) {

    fun execute(id: Long, @Valid @RequestBody request: UpdateProductRequest): ProductDto {
        val product = productRepository.findById(id)
            .orElseThrow { NotFoundException("Product not found") }

        product.name = request.name

        val saved = productRepository.save(product)

        return ProductDto(saved.id, saved.name)
    }
}