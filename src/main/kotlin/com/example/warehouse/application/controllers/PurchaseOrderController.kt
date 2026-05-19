package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.purchaseorder.CreatePurchaseOrderRequest
import com.example.warehouse.application.dto.purchaseorder.PurchaseOrderDto
import com.example.warehouse.application.dto.purchaseorder.UpdatePurchaseOrderStatusRequest
import com.example.warehouse.application.services.purchase.CreatePurchaseOrder
import com.example.warehouse.application.services.purchase.DeletePurchaseOrder
import com.example.warehouse.application.services.purchase.GetPurchaseOrderById
import com.example.warehouse.application.services.purchase.GetPurchaseOrders
import com.example.warehouse.application.services.purchase.UpdatePurchaseOrder
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchase-orders")
class PurchaseOrderController(
    private val createUseCase: CreatePurchaseOrder,
    private val getUseCase: GetPurchaseOrders,
    private val getByIdUseCase: GetPurchaseOrderById,
    private val deleteUseCase: DeletePurchaseOrder,
    private val updateUseCase: UpdatePurchaseOrder
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreatePurchaseOrderRequest): ResponseEntity<PurchaseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createUseCase.execute(request))
    }

    @GetMapping
    fun getAll(): List<PurchaseOrderDto> {
        return getUseCase.execute()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): PurchaseOrderDto {
        return getByIdUseCase.execute(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @PutMapping("/{id}")
    fun updateStatus(@PathVariable id: Long,@Valid @RequestBody request: UpdatePurchaseOrderStatusRequest
    ): PurchaseOrderDto {
        return updateUseCase.execute(id, request)
    }
}