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
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(
    properties = ["spring.config.location=src/test/resources/application-test.yaml"]
)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
class SupplierIntegrationTest {

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
    fun `POST supplier creates supplier`() {

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": "Tech Supplier"
                }
                """.trimIndent()
            )

        } When {

            post("/suppliers")

        } Then {

            statusCode(201)

            body("id", notNullValue())
            body("name", equalTo("Tech Supplier"))
        }
    }

    @Test
    fun `GET supplier by id returns supplier`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "name": "Supplier One"
                    }
                    """.trimIndent()
                )

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId = answer.extract().body().path<Int>("id")

        Given { this
        } When {

            get("/suppliers/$supplierId")

        } Then {

            statusCode(200)

            body("id", equalTo(supplierId))
            body("name", equalTo("Supplier One"))
        }
    }

    @Test
    fun `GET all suppliers returns list`() {

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": "Global Supplier",
                    "email": "global@test.com"
                }
                """.trimIndent()
            )

        } When {

            post("/suppliers")

        } Then {
        }

        Given { this
        } When {

            get("/suppliers")

        } Then {

            statusCode(200)

            body("size()", greaterThan(0))
        }
    }

    @Test
    fun `PUT supplier updates supplier`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "name": "Old Supplier"
                    }
                    """.trimIndent()
                )

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId = answer.extract().body().path<Int>("id")

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": "Updated Supplier"
                }
                """.trimIndent()
            )

        } When {

            put("/suppliers/$supplierId")

        } Then {

            statusCode(200)

            body("id", equalTo(supplierId))
            body("name", equalTo("Updated Supplier"))
        }
    }

    @Test
    fun `DELETE supplier removes supplier`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "name": "Temporary Supplier"
                    }
                    """.trimIndent()
                )

            } When {

                post("/suppliers")

            } Then {
            }

        val supplierId = answer.extract().body().path<Int>("id")

        Given { this
        } When {

            delete("/suppliers/$supplierId")

        } Then {

            statusCode(204)
        }

        Given { this
        } When {

            get("/suppliers/$supplierId")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `GET nonexistent supplier returns 404`() {

        Given { this
        } When {

            get("/suppliers/999999")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `POST supplier with blank name returns 400`() {

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": "",
                    "email": "supplier@test.com"
                }
                """.trimIndent()
            )

        } When {

            post("/suppliers")

        } Then {

            statusCode(400)
        }
    }

}