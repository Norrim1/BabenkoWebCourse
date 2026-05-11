package com.example.warehouse.application.auth

import com.example.warehouse.infrastructure.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {

        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found")

        return User(
            user.email,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it.name) }
        )
    }
}