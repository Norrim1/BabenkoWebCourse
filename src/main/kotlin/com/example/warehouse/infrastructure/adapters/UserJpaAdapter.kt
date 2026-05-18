package com.example.warehouse.infrastructure.adapters

import com.example.warehouse.application.ports.SupplierRepositoryPort
import com.example.warehouse.application.ports.UserRepositoryPort
import com.example.warehouse.domain.entities.supplier.SupplierEntity
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.infrastructure.repositories.UserRepository
import org.springframework.stereotype.Component

@Component
class UserJpaAdapter(
    private val repository: UserRepository
) : UserRepositoryPort {
    override fun findByEmail(email: String): UserEntity? {
        return repository.findByEmail(email)
    }
}