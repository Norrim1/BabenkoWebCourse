package com.example.warehouse.application.usecases.supplier

import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.dto.supplier.UpdateSupplierRequest
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class UpdateSupplier(
    private val supplierRepository: SupplierRepositoryPort
) {

    fun execute(id: Long, @Valid @RequestBody request: UpdateSupplierRequest): SupplierDto {
        val supplier = supplierRepository.findById(id)
            ?: throw NotFoundException("Supplier not found")

        supplier.name = request.name

        val saved = supplierRepository.save(supplier)

        return SupplierDto(saved.id, saved.name)
    }
}