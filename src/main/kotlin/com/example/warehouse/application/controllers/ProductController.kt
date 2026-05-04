package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.product.CreateProductRequest
import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.application.dto.product.UpdateProductRequest
import com.example.warehouse.application.usecases.product.CreateProduct
import com.example.warehouse.application.usecases.product.DeleteProduct
import com.example.warehouse.application.usecases.product.GetProduct
import com.example.warehouse.application.usecases.product.UpdateProduct
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val createUseCase: CreateProduct,
    private val getUseCase: GetProduct,
    private val deleteUseCase: DeleteProduct,
    private val updateUseCase: UpdateProduct
) {

    @PostMapping
    fun create(@RequestBody request: CreateProductRequest): ProductDto {
        return createUseCase.execute(request)
    }

    @GetMapping
    fun getAll(): List<ProductDto> {
        return getUseCase.execute()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateProductRequest
    ): ProductDto {
        return updateUseCase.execute(id, request)
    }
}