package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.warehouse.CreateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.UpdateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.usecases.warehouse.CreateWarehouse
import com.example.warehouse.application.usecases.warehouse.DeleteWarehouse
import com.example.warehouse.application.usecases.warehouse.GetWarehouse
import com.example.warehouse.application.usecases.warehouse.UpdateWarehouse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/warehouses")
class WarehouseController(
    private val createWarehouseUseCase: CreateWarehouse,
    private val getWarehousesUseCase: GetWarehouse,
    private val updateUseCase: UpdateWarehouse,
    private val deleteUseCase: DeleteWarehouse
) {

    @PostMapping
    fun create(@RequestBody request: CreateWarehouseRequest): WarehouseDto {
        return createWarehouseUseCase.execute(request)
    }

    @GetMapping
    fun getAll(): List<WarehouseDto> {
        return getWarehousesUseCase.execute()
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateWarehouseRequest
    ): WarehouseDto {
        return updateUseCase.execute(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }
}