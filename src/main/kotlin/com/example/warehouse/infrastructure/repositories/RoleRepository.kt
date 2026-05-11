package com.example.warehouse.infrastructure.repositories

import com.example.warehouse.domain.entities.roles.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleEntity, Long> {
    fun findByName(name: String): RoleEntity?
}