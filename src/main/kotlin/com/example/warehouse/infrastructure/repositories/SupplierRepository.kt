package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.supplier.SupplierEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierRepository: JpaRepository<SupplierEntity, Long>