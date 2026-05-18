package com.example.warehouse.controller

import com.example.warehouse.infrastructure.repositories.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(properties = ["spring.config.location=src/test/resources/application-test.yaml"])
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
class RoleCheckTest {

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Test
    fun `roles are loaded from database`() {
        val roles = roleRepository.findAll()

        println(roles)

        assert(roles.isNotEmpty())
    }
}