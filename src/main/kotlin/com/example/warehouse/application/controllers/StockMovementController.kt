package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.stockmovement.StockMovementDto
import com.example.warehouse.application.usecases.stockmovement.GetStockMovement
import com.example.warehouse.application.usecases.stockmovement.GetStockMovementById
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock/movements")
class StockMovementController(

    private val getAllUseCase: GetStockMovement,
    private val getByIdUseCase: GetStockMovementById
) {

    @GetMapping
    fun getAll(): List<StockMovementDto> {
        return getAllUseCase.execute()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): StockMovementDto {
        return getByIdUseCase.execute(id)
    }
}