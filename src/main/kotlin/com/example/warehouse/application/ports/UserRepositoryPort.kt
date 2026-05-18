package com.example.warehouse.application.ports

import com.example.warehouse.domain.entities.user.UserEntity

interface UserRepositoryPort {
    fun findByEmail(email: String): UserEntity?
}