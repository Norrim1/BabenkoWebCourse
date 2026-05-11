package com.example.warehouse.application.auth

import org.springframework.security.core.context.SecurityContextHolder

object SecUtils {

    fun getCurrentUserEmail(): String {

        return SecurityContextHolder.getContext()
            .authentication!!
            .name
    }
}