package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}