package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity

interface StockMovementRepositoryPort {

    fun save(movement: StockMovementEntity): StockMovementEntity

    fun findById(id: Long): StockMovementEntity?

    fun findAll(): List<StockMovementEntity>
}