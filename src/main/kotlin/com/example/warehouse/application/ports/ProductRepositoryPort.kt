package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.product.Product
import com.example.warehouse.domain.entities.product.ProductEntity
import java.util.*

interface ProductRepositoryPort {

    fun save(product: ProductEntity): ProductEntity

    fun findById(id: Long): ProductEntity?

    fun findAll(): List<ProductEntity>

    fun deleteById(id: Long)
}