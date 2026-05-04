package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.product.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long>