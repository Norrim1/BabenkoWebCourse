package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.purchaseorderitem.PurchaseOrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderItemRepository : JpaRepository <PurchaseOrderItemEntity, Long>