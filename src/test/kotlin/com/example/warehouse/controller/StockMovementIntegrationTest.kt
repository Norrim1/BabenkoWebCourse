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
import org.hamcrest.Matchers.greaterThan
import org.mockito.internal.matchers.GreaterThan
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
class StockMovementIntegrationTest {

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
    fun `GET stock movement by id returns movement`() {

        val token = registerAndGetToken()

        val warehouseAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")

                body("""{"name":"Main Warehouse"}""")

            } When {

                post("/warehouses")

            } Then {
            }

        val warehouseId =
            warehouseAnswer.extract().body().path<Int>("id")

        val productAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")

                body("""{"name":"Laptop"}""")

            } When {

                post("/products")

            } Then {
            }

        val productId =
            productAnswer.extract().body().path<Int>("id")

        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")

            body(
                """
                {
                    "warehouseId": $warehouseId,
                    "productId": $productId,
                    "quantity": 15,
                    "reason": "Initial stock"
                }
                """.trimIndent()
            )

        } When {

            post("/stock/inbound")

        } Then {

            statusCode(200)
        }

        val movementAnswer =
            Given {

                header("Authorization", "Bearer $token")

            } When {

                get("/stock/movements")

            } Then {

                statusCode(200)

                body("size()", greaterThan(0))

                extract()
                    .response()
            }

        val movementId =
            movementAnswer.extract().body().path<Int>("[0].id")

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/stock/movements/$movementId")

        } Then {

            statusCode(200)

            body("id", equalTo(movementId))
            body("quantity", equalTo(15))
        }
    }

    @Test
    fun `GET nonexistent stock movement returns 404`() {

        val token = registerAndGetToken()

        Given {

            header("Authorization", "Bearer $token")

        } When {

            get("/stock/movements/999999")

        } Then {

            statusCode(404)
        }
    }
}