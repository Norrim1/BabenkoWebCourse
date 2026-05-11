package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.warehouse.WarehouseEntity

interface WarehouseRepositoryPort {

    fun save(warehouse: WarehouseEntity): WarehouseEntity

    fun findById(id: Long): WarehouseEntity?

    fun findAll(): List<WarehouseEntity>

    fun deleteById(id: Long)
}