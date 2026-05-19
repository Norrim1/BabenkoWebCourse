package com.example.warehouse.application.services.product

import com.example.warehouse.application.dto.product.CreateProductRequest
import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.domain.entities.product.ProductEntity
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody


@Service
class CreateProduct(
    private val productRepository: ProductRepositoryPort
) {

    fun execute(@Valid @RequestBody request: CreateProductRequest): ProductDto {
        val product = ProductEntity(null, request.name)
        val saved = productRepository.save(product)

        return ProductDto(saved.id, saved.name)
    }
}