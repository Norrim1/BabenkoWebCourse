package com.example.warehouse.application.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class RegisterRequest(

    @field:Email
    val email: String,

    @field:Size(min = 4)
    val password: String,

    val role: String
)