package com.example.warehouse.application.usecases.supplier

import com.example.warehouse.application.dto.supplier.CreateSupplierRequest
import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.domain.entities.supplier.SupplierEntity
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class CreateSupplier(
    private val supplierRepository: SupplierRepository
) {

    fun execute(@Valid @RequestBody request: CreateSupplierRequest): SupplierDto {
        val supplier = SupplierEntity(null, request.name)
        val saved = supplierRepository.save(supplier)

        return SupplierDto(saved.id, saved.name)
    }
}