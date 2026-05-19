package com.example.warehouse.application.auth

import com.example.warehouse.application.dto.auth.AuthResponse
import com.example.warehouse.application.dto.auth.LoginRequest
import com.example.warehouse.application.dto.auth.RegisterRequest
import com.example.warehouse.domain.entities.user.UserEntity
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.domain.exceptions.NotFoundException
import com.example.warehouse.infrastructure.repositories.RoleRepository
import com.example.warehouse.infrastructure.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    fun register(request: RegisterRequest): AuthResponse {

        if (userRepository.findByEmail(request.email) != null) {
            throw ConflictException("User already exists")
        }

        val role = roleRepository.findByName(request.role)
            ?: throw NotFoundException("Role not found")

        val user = UserEntity(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            roles = setOf(role)
        )

        userRepository.save(user)

        val token = jwtService.generateToken(
            user.email,
            user.roles.map { it.name }
        )

        return AuthResponse(token)
    }

    fun login(request: LoginRequest): AuthResponse {

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userRepository.findByEmail(request.email)
            ?: throw NotFoundException("User not found")

        val token = jwtService.generateToken(
            user.email,
            user.roles.map { it.name }
        )

        return AuthResponse(token)
    }
}