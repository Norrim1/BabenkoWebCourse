package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.stockmovement.StockMovementEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StockMovementRepository: JpaRepository<StockMovementEntity, Long>