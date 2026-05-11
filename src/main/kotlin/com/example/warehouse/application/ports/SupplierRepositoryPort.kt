package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.supplier.SupplierEntity

interface SupplierRepositoryPort {

    fun save(supplier: SupplierEntity): SupplierEntity

    fun findById(id: Long): SupplierEntity?

    fun findAll(): List<SupplierEntity>

    fun deleteById(id: Long)
}