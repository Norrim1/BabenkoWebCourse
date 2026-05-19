package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.product.CreateProductRequest
import com.example.warehouse.application.dto.product.ProductDto
import com.example.warehouse.application.dto.product.UpdateProductRequest
import com.example.warehouse.application.services.product.CreateProduct
import com.example.warehouse.application.services.product.DeleteProduct
import com.example.warehouse.application.services.product.GetProduct
import com.example.warehouse.application.services.product.GetProductById
import com.example.warehouse.application.services.product.UpdateProduct
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
@RequestMapping("/products")
class ProductController(
    private val createUseCase: CreateProduct,
    private val getUseCase: GetProduct,
    private val deleteUseCase: DeleteProduct,
    private val updateUseCase: UpdateProduct,
    private val getByIdUseCase: GetProductById
) {

    @PostMapping
    @CacheEvict(value = ["products"], allEntries = true)
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createUseCase.execute(request))
    }

    @GetMapping
    @Cacheable(value = ["products"])
    fun getAll(): List<ProductDto> {
        return getUseCase.execute()
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = ["products"], allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @PutMapping("/{id}")
    @CacheEvict(value = ["products"], allEntries = true)
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ProductDto {
        return updateUseCase.execute(id, request)
    }

    @GetMapping("/{id}")
    @Cacheable(value = ["products"], key = "#id")
    fun getById(
        @PathVariable id: Long
    ): ProductDto {
        return getByIdUseCase.execute(id)
    }
}