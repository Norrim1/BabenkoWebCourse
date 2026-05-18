package com.example.warehouse.controller

import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import java.util.UUID

@SpringBootTest
@TestPropertySource(
    properties = ["spring.config.location=src/test/resources/application-test.yaml"]
)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class SecurityIntegrationTest {

    companion object {

        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
            withDatabaseName("postgres")
            withUsername("postgres")
            withPassword("postgres")
        }
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    private fun registerAndGetToken(
        role: String = "ROLE_WAREHOUSE_OPERATOR"
    ): String {
        val answer =
            Given {
                contentType(ContentType.JSON)
                body(
                    """{
                    "email": "test-${UUID.randomUUID()}@test.com",
                    "password": "password123",
                    "role": "$role"}""".trimIndent()
                )
            } When {
                post("/auth/register")
            } Then {
                statusCode(201)
            }
        return answer.extract()
            .body()
            .path<String>("token")
    }

    @Test
    fun `unauthorized user cannot access stock movements`() {

        Given { this
        } When {

            get("/stock/movements")

        } Then {

            statusCode(401)
        }
    }

    @Test
    fun `invalid jwt token returns 401`() {

        Given {

            header("Authorization", "Bearer invalid-token")

        } When {

            get("/stock/movements")

        } Then {

            statusCode(401)
        }
    }

    @Test
    fun `warehouse operator cannot access suppliers`() {

        val token =
            registerAndGetToken("ROLE_WAREHOUSE_OPERATOR")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/suppliers")

        } Then {

            statusCode(403)
        }
    }

    @Test
    fun `warehouse operator cannot access purchase orders`() {

        val token =
            registerAndGetToken("ROLE_WAREHOUSE_OPERATOR")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/purchase-orders")

        } Then {

            statusCode(403)
        }
    }

    @Test
    fun `procurement manager can access suppliers`() {

        val token =
            registerAndGetToken("ROLE_PROCUREMENT_MANAGER")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/suppliers")

        } Then {

            statusCode(200)
        }
    }

    @Test
    fun `procurement manager can access purchase orders`() {

        val token =
            registerAndGetToken("ROLE_PROCUREMENT_MANAGER")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/purchase-orders")

        } Then {

            statusCode(200)
        }
    }

    @Test
    fun `warehouse operator can access stock movements`() {

        val token =
            registerAndGetToken("ROLE_WAREHOUSE_OPERATOR")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/stock/movements")

        } Then {

            statusCode(200)
        }
    }
}