package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WarehouseRepository: JpaRepository<WarehouseEntity, Long>