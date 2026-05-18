package com.example.warehouse.controller

import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.internal.MockMvcRestAssuredResponseImpl
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
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(
    properties = ["spring.config.location=src/test/resources/application-test.yaml"]
)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
class PurchaseOrderIntegrationTest {

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

    @Test
    fun `POST purchase order creates order`() {

        val supplierAnswer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Supplier"}""")

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId =
            supplierAnswer.extract().body().path<Int>("id")

        val productAnswer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Laptop"}""")

            } When {

                post("/products")

            } Then {
            }

        val productId =
            productAnswer.extract().body().path<Int>("id")

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "supplierId": $supplierId,
                    "items": [
                        {
                            "productId": $productId,
                            "quantity": 10
                        }
                    ]
                }
                """.trimIndent()
            )

        } When {

            post("/purchase-orders")

        } Then {

            statusCode(201)

            body("id", notNullValue())
        }
    }

    @Test
    fun `GET purchase order by id returns order`() {

        val supplierAnswer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Supplier"}""")

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId =
            supplierAnswer.extract().body().path<Int>("id")

        val orderAnswer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "supplierId": $supplierId,
                        "items": []
                    }
                    """.trimIndent()
                )

            } When {

                post("/purchase-orders")

            } Then {
            }

        val orderId =
            orderAnswer.extract().body().path<Int>("id")

        Given { this
        } When {

            get("/purchase-orders/$orderId")

        } Then {

            statusCode(200)

            body("id", equalTo(orderId))
        }
    }

    @Test
    fun `DELETE purchase order removes order`() {

        val supplierAnswer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Supplier"}""")

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId =
            supplierAnswer.extract().body().path<Int>("id")

        val orderAnswer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "supplierId": $supplierId,
                        "items": []
                    }
                    """.trimIndent()
                )

            } When {

                post("/purchase-orders")

            } Then {
            }

        val orderId =
            orderAnswer.extract().body().path<Int>("id")

        Given { this
        } When {

            delete("/purchase-orders/$orderId")

        } Then {

            statusCode(204)
        }
    }
}