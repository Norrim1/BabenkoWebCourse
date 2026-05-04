package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository : JpaRepository<PurchaseOrderEntity, Long>