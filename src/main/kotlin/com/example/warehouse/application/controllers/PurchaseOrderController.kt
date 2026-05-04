package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.purchaseorder.CreatePurchaseOrderRequest
import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.usecases.purchase.CreatePurchaseOrder
import com.example.warehouse.application.usecases.purchase.GetPurchaseOrders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchase-orders")
class PurchaseOrderController(
    private val createUseCase: CreatePurchaseOrder,
    private val getUseCase: GetPurchaseOrders
) {

    @PostMapping
    fun create(@RequestBody request: CreatePurchaseOrderRequest): PurchaseOrderDto {
        return createUseCase.execute(request)
    }

    @GetMapping
    fun getAll(): List<PurchaseOrderDto> {
        return getUseCase.execute()
    }
}