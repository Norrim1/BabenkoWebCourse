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
@TestPropertySource(properties = ["spring.config.location=src/test/resources/application-test.yaml"])
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class StockBalanceIntegrationTest {

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
    fun `POST inbound increases stock balance`() {
        val token = registerAndGetToken()
        val warehouseAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body(
                    """
                {
                    "name": "Main Warehouse"
                }
                """.trimIndent()
                )

            } When {

                post("/warehouses")

            } Then {
            }

        val warehouseId =
            warehouseAnswer.extract()
                .body()
                .path<Int>("id")

        val productAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body(
                    """
                {
                    "name": "Laptop"
                }
                """.trimIndent()
                )

            } When {

                post("/products")

            } Then {
            }

        val productId =
            productAnswer.extract()
                .body()
                .path<Int>("id")

        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")
            body(
                """
            {
                "warehouseId": $warehouseId,
                "productId": $productId,
                "quantity": 10,
                "reason": "Supplier delivery"
            }
            """.trimIndent()
            )

        } When {

            post("/stock/inbound")

        } Then {

            statusCode(200)
        }

        Given {
            header("Authorization", "Bearer $token")
        } When {

            get("/stock/balances/$warehouseId")

        } Then {

            statusCode(200)

            body("[0].quantity", equalTo(10))
        }
    }

    @Test
    fun `POST outbound decreases stock balance`() {
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
        val warehouseId = warehouseAnswer.extract().body().path<Int>("id")
        val productAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Keyboard"}""")

            } When {

                post("/products")

            } Then {
            }
        val productId = productAnswer.extract().body().path<Int>("id")
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
        }

        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")
            body(
                """
            {
                "warehouseId": $warehouseId,
                "productId": $productId,
                "quantity": 5,
                "reason": "Customer order"
            }
            """.trimIndent()
            )

        } When {

            post("/stock/outbound")

        } Then {

            statusCode(200)
        }

        Given {
            header("Authorization", "Bearer $token")
        } When {

            get("/stock/balances/$warehouseId")

        } Then {

            statusCode(200)

            body("[0].quantity", equalTo(10))
        }
    }

    @Test
    fun `POST outbound with insufficient stock returns 400`() {
        val token = registerAndGetToken()
        val warehouseAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Warehouse"}""")

            } When {

                post("/warehouses")

            } Then {
            }
        val warehouseId = warehouseAnswer.extract().body().path<Int>("id")
        val productAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Mouse"}""")

            } When {

                post("/products")

            } Then {
            }
        val productId = productAnswer.extract().body().path<Int>("id")
        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")
            body(
                """
            {
                "warehouseId": $warehouseId,
                "productId": $productId,
                "quantity": 50,
                "reason": "Invalid outbound"
            }
            """.trimIndent()
            )

        } When {

            post("/stock/outbound")

        } Then {

            statusCode(400)
        }
    }

    @Test
    fun `POST transfer updates both warehouse balances`() {
        val token = registerAndGetToken()
        val warehouseAnswerA =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Warehouse A"}""")

            } When {

                post("/warehouses")

            } Then {
            }
        val warehouseAnswerB =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Warehouse B"}""")

            } When {

                post("/warehouses")

            } Then {
            }
        val warehouseIdA = warehouseAnswerA.extract().body().path<Int>("id")
        val warehouseIdB = warehouseAnswerB.extract().body().path<Int>("id")
        val productAnswer =
            Given {

                contentType(ContentType.JSON)
                header("Authorization", "Bearer $token")
                body("""{"name":"Monitor"}""")

            } When {

                post("/products")

            } Then {
            }
        val productId = productAnswer.extract().body().path<Int>("id")
        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")
            body(
                """
            {
                "warehouseId": $warehouseIdA,
                "productId": $productId,
                "quantity": 20,
                "reason": "Initial stock"
            }
            """.trimIndent()
            )

        } When {

            post("/stock/inbound")

        } Then {
        }

        Given {

            contentType(ContentType.JSON)
            header("Authorization", "Bearer $token")
            body(
                """
            {
                "fromWarehouseId": $warehouseIdA,
                "toWarehouseId": $warehouseIdB,
                "productId": $productId,
                "quantity": 5,
                "reason": "Internal transfer"
            }
            """.trimIndent()
            )

        } When {

            post("/stock/transfer")

        } Then {

            statusCode(200)
        }

        Given {
            header("Authorization", "Bearer $token")
        } When {

            get("/stock/balances/$warehouseIdA")

        } Then {

            statusCode(200)

            body("find { it.warehouseId == $warehouseIdA }.quantity", equalTo(15))
        }
        Given {
            header("Authorization", "Bearer $token")
        } When {

            get("/stock/balances/$warehouseIdB")

        } Then {

            statusCode(200)

            body("find { it.warehouseId == $warehouseIdB }.quantity", equalTo(5))
        }
    }
}