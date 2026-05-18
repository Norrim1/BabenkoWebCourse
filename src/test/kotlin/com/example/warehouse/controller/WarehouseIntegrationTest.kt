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
@TestPropertySource(properties = ["spring.config.location=src/test/resources/application-test.yaml"])
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
class WarehouseIntegrationTest {

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
    fun `POST warehouse returns 201 and creates warehouse`() {

        Given {
            contentType(ContentType.JSON)

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

            statusCode(201)

            body("id", notNullValue())
            body("name", equalTo("Main Warehouse"))
        }
    }

    @Test
    fun `GET warehouse by invalid id returns 404`() {
        Given { this
        } When {

            get("/warehouses/999999")

        } Then {

            statusCode(404)
            body("status", equalTo(404))
        }
    }


    @Test
    fun `POST warehouse with blank name returns 400`() {

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": ""
                }
                """.trimIndent()
            )

        } When {

            post("/warehouses")

        } Then {

            statusCode(400)

            body("status", equalTo(400))
            body("error", notNullValue())
        }
    }

    @Test
    fun `GET warehouse by id returns warehouse`() {

        val answer =
            Given {
                contentType(ContentType.JSON)

                body(
                    """
                {
                    "name": "Central Warehouse"
                }
                """.trimIndent()
                )

            } When {

                post("/warehouses")

            } Then {

                statusCode(201)
            }
        val createdId = answer.extract().body().path<Int>("id")
        Given { this
        } When {

            get("/warehouses/$createdId")

        } Then {

            statusCode(200)

            body("id", equalTo(createdId))
            body("name", equalTo("Central Warehouse"))
        }
    }

    @Test
    fun `GET all warehouses returns list`() {

        Given {
            contentType(ContentType.JSON)

            body(
                """
            {
                "name": "Warehouse A"
            }
            """.trimIndent()
            )

        } When {
            post("/warehouses")
        }

        Given {
            contentType(ContentType.JSON)

            body(
                """
            {
                "name": "Warehouse B"
            }
            """.trimIndent()
            )

        } When {
            post("/warehouses")
        }
        Given { this
        } When {

            get("/warehouses")

        } Then {

            statusCode(200)

            body("size()", greaterThanOrEqualTo(2))
        }
    }

    @Test
    fun `PUT warehouse updates warehouse`() {

        val answer =
            Given {
                contentType(ContentType.JSON)

                body(
                    """
                {
                    "name": "Old Name"
                }
                """.trimIndent()
                )

            } When {

                post("/warehouses")

            } Then {
            }
        val createdId = answer.extract().body().path<Int>("id")
        Given {

            contentType(ContentType.JSON)

            body(
                """
            {
                "name": "Updated Warehouse"
            }
            """.trimIndent()
            )

        } When {

            put("/warehouses/$createdId")

        } Then {

            statusCode(200)

            body("id", equalTo(createdId))
            body("name", equalTo("Updated Warehouse"))
        }
    }

    @Test
    fun `PUT nonexistent warehouse returns 404`() {

        Given {

            contentType(ContentType.JSON)

            body(
                """
            {
                "name": "Updated Warehouse"
            }
            """.trimIndent()
            )

        } When {

            put("/warehouses/999999")

        } Then {

            statusCode(404)

            body("status", equalTo(404))
        }
    }

    @Test
    fun `DELETE warehouse removes warehouse`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                {
                    "name": "Warehouse To Delete"
                }
                """.trimIndent()
                )

            } When {

                post("/warehouses")

            } Then {
            }
        val createdId = answer.extract().body().path<Int>("id")
        Given { this
        } When {

            delete("/warehouses/$createdId")

        } Then {

            statusCode(204)
        }
        Given { this
        } When {

            get("/warehouses/$createdId")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `DELETE nonexistent warehouse returns 404`() {
        Given { this
        } When {

            delete("/warehouses/999999")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `POST warehouse with too long name returns 400`() {

        val longName = "A".repeat(300)

        Given {

            contentType(ContentType.JSON)

            body(
                """
            {
                "name": "$longName"
            }
            """.trimIndent()
            )

        } When {

            post("/warehouses")

        } Then {

            statusCode(400)
        }
    }
}