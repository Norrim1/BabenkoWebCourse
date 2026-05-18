package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.supplier.CreateSupplierRequest
import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.dto.supplier.UpdateSupplierRequest
import com.example.warehouse.application.usecases.supplier.CreateSupplier
import com.example.warehouse.application.usecases.supplier.DeleteSupplier
import com.example.warehouse.application.usecases.supplier.GetSupplier
import com.example.warehouse.application.usecases.supplier.GetSupplierById
import com.example.warehouse.application.usecases.supplier.UpdateSupplier
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
@RequestMapping("/suppliers")
class SupplierController(
    private val createUseCase: CreateSupplier,
    private val getUseCase: GetSupplier,
    private val deleteUseCase: DeleteSupplier,
    private val updateUseCase: UpdateSupplier,
    private val getByIdUseCase: GetSupplierById
) {

    @PostMapping
    @CacheEvict(value = ["suppliers"], allEntries = true)
    fun create(@Valid @RequestBody request: CreateSupplierRequest): ResponseEntity<SupplierDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createUseCase.execute(request))
    }

    @GetMapping
    @Cacheable(value = ["suppliers"])
    fun getAll(): List<SupplierDto> {
        return getUseCase.execute()
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = ["suppliers"], allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        deleteUseCase.execute(id)
    }

    @PutMapping("/{id}")
    @CacheEvict(value = ["suppliers"], allEntries = true)
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateSupplierRequest
    ): SupplierDto {
        return updateUseCase.execute(id, request)
    }

    @GetMapping("/{id}")
    @Cacheable(value = ["suppliers"], key = "#id")
    fun getById(
        @PathVariable id: Long
    ): SupplierDto{
        return getByIdUseCase.execute(id)
    }
}