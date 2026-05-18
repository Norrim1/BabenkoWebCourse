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
class ProductIntegrationTest {

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

    @BeforeEach
    fun setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `POST product creates product`() {

        Given {

            contentType(ContentType.JSON)

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

            statusCode(201)

            body("id", notNullValue())
            body("name", equalTo("Laptop"))
        }
    }

    @Test
    fun `GET product by id returns product`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body(
                    """
                    {
                        "name": "Keyboard"
                    }
                    """.trimIndent()
                )

            } When {

                post("/products")

            } Then {
            }

        val productId = answer.extract().body().path<Int>("id")

        Given {
            this

        } When {

            get("/products/$productId")

        } Then {

            statusCode(200)

            body("id", equalTo(productId))
            body("name", equalTo("Keyboard"))
        }
    }

    @Test
    fun `GET nonexistent product returns 404`() {

        Given { this

        } When {

            get("/products/999999")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `GET all products returns list`() {

        Given {

            contentType(ContentType.JSON)

            body("""{"name":"Mouse"}""")

        } When {

            post("/products")

        } Then {
        }

        Given {
            this
        } When {

            get("/products")

        } Then {

            statusCode(200)

            body("size()", greaterThan(0))
        }
    }

    @Test
    fun `PUT product updates product`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Old Product"}""")

            } When {

                post("/products")

            } Then {
            }

        val productId = answer.extract().body().path<Int>("id")

        Given {

            contentType(ContentType.JSON)

            body(
                """
                {
                    "name": "Updated Product"
                }
                """.trimIndent()
            )

        } When {

            put("/products/$productId")

        } Then {

            statusCode(200)

            body("id", equalTo(productId))
            body("name", equalTo("Updated Product"))
        }
    }

    @Test
    fun `DELETE product removes product`() {

        val answer =
            Given {

                contentType(ContentType.JSON)

                body("""{"name":"Temporary Product"}""")

            } When {

                post("/products")

            } Then {
            }

        val productId = answer.extract().body().path<Int>("id")

        Given {
            this
        } When {

            delete("/products/$productId")

        } Then {

            statusCode(204)
        }

        Given {
            this
        } When {

            get("/products/$productId")

        } Then {

            statusCode(404)
        }
    }

    @Test
    fun `POST product with blank name returns 400`() {

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

            post("/products")

        } Then {

            statusCode(400)
        }
    }
}