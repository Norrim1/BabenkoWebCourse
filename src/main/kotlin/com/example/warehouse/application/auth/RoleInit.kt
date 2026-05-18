package com.example.warehouse.application.auth

import com.example.warehouse.domain.entities.roles.RoleEntity
import com.example.warehouse.infrastructure.repositories.RoleRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class RoleInit(
    private val roleRepository: RoleRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun init() {

        val roles = listOf(
            "ROLE_WAREHOUSE_OPERATOR",
            "ROLE_PROCUREMENT_MANAGER"
        )

        roles.forEach {
            if (roleRepository.findByName(it) == null) {
                roleRepository.save(RoleEntity(name = it))
            }
        }
    }
}