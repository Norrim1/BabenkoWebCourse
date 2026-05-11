package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.purchaseorder.PurchaseOrderEntity

interface PurchaseOrderRepositoryPort {

    fun save(order: PurchaseOrderEntity): PurchaseOrderEntity

    fun findById(id: Long): PurchaseOrderEntity?

    fun findAll(): List<PurchaseOrderEntity>

    fun deleteById(id: Long)
}