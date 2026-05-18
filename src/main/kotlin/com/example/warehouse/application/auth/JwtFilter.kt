package com.example.warehouse.application.auth

import com.example.warehouse.application.ports.UserRepositoryPort
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtService: JwtService,
    private val userRepositoryPort: UserRepositoryPort
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.removePrefix("Bearer ")

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response)
            return
        }
        val email = jwtService.extractEmail(token)
        val user = userRepositoryPort.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found: $email")
        val roles = jwtService.extractRoles(token)
        val auth = UsernamePasswordAuthenticationToken(
            user,
            null,
            roles.map { SimpleGrantedAuthority(it) }
        )

        SecurityContextHolder.getContext().authentication = auth

        filterChain.doFilter(request, response)
    }
}