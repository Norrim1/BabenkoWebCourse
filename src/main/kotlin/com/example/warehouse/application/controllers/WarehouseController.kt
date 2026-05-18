package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.stockmovement.StockMovementDto
import com.example.warehouse.application.dto.warehouse.CreateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.UpdateWarehouseRequest
import com.example.warehouse.application.dto.warehouse.WarehouseDto
import com.example.warehouse.application.usecases.stockmovement.GetStockMovementById
import com.example.warehouse.application.usecases.warehouse.CreateWarehouse
import com.example.warehouse.application.usecases.warehouse.DeleteWarehouse
import com.example.warehouse.application.usecases.warehouse.GetWarehouse
import com.example.warehouse.application.usecases.warehouse.GetWarehouseById
import com.example.warehouse.application.usecases.warehouse.UpdateWarehouse
import com.example.warehouse.domain.entities.warehouse.WarehouseEntity
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
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
@RequestMapping("/warehouses")
class WarehouseController(
    private val createWarehouseUseCase: CreateWarehouse,
    private val getWarehousesUseCase: GetWarehouse,
    private val updateUseCase: UpdateWarehouse,
    private val deleteUseCase: DeleteWarehouse,
    private val getByIdUseCase: GetWarehouseById
) {

    @PostMapping
    @CacheEvict(value = ["warehouses"], allEntries = true)
    fun create(@Valid @RequestBody request: CreateWarehouseRequest): ResponseEntity<WarehouseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createWarehouseUseCase.execute(request))
    }

    @GetMapping
    @Cacheable(value = ["warehouses"])
    fun getAll(): List<WarehouseDto> {
        return getWarehousesUseCase.execute()
    }

    @PutMapping("/{id}")
    @CacheEvict(value = ["warehouses"], allEntries = true)
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateWarehouseRequest
    ): WarehouseDto {
        return updateUseCase.execute(id, request)
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = ["warehouses"], allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @GetMapping("/{id}")
    @Cacheable(value = ["warehouses"], key = "#id")
    fun getById(
        @PathVariable id: Long
    ): WarehouseDto {
        return getByIdUseCase.execute(id)
    }
}