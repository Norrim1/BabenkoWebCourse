package com.example.warehouse.application.usecases.supplier

import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.infrastructure.repositories.SupplierRepository
import org.springframework.stereotype.Service

@Service
class GetSupplier(
    private val supplierRepository: SupplierRepositoryPort
) {

    fun execute(): List<SupplierDto> {
        return supplierRepository.findAll().map {
            SupplierDto(it.id, it.name)
        }
    }
}