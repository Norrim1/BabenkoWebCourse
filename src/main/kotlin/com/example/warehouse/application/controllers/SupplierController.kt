package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.supplier.CreateSupplierRequest
import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.dto.supplier.UpdateSupplierRequest
import com.example.warehouse.application.usecases.supplier.CreateSupplier
import com.example.warehouse.application.usecases.supplier.DeleteSupplier
import com.example.warehouse.application.usecases.supplier.GetSupplier
import com.example.warehouse.application.usecases.supplier.UpdateSupplier
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/suppliers")
class SupplierController(
    private val createUseCase: CreateSupplier,
    private val getUseCase: GetSupplier,
    private val deleteUseCase: DeleteSupplier,
    private val updateUseCase: UpdateSupplier
) {

    @PostMapping
    fun create(@RequestBody request: CreateSupplierRequest): SupplierDto {
        return createUseCase.execute(request)
    }

    @GetMapping
    fun getAll(): List<SupplierDto> {
        return getUseCase.execute()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateSupplierRequest
    ): SupplierDto {
        return updateUseCase.execute(id, request)
    }
}