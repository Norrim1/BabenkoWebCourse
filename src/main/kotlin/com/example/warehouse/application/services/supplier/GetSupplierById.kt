package com.example.warehouse.application.services.supplier

import com.example.warehouse.application.dto.supplier.SupplierDto
import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class GetSupplierById (
    private val repository: SupplierRepositoryPort
) {
    fun execute(id: Long): SupplierDto {

        val supplier = repository.findById(id)
            ?: throw NotFoundException("Movement not found")

        return SupplierDto(
            id = supplier.id,
            name = supplier.name
        )
    }
}