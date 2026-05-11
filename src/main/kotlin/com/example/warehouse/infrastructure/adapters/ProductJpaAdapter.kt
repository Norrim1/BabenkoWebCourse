package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.ProductRepositoryPort
import com.example.warehouse.domain.entities.product.Product
import com.example.warehouse.domain.entities.product.ProductEntity
import com.example.warehouse.infrastructure.repositories.ProductRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductJpaAdapter(
    private val repository: ProductRepository
) : ProductRepositoryPort {

    override fun save(product: ProductEntity): ProductEntity {
        return repository.save(product)
    }

    override fun findById(id: Long): ProductEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(): List<ProductEntity> {
        return repository.findAll()
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}