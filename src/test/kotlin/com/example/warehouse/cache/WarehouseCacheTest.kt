package com.example.warehouse.cache

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.junit.jupiter.Container
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.springframework.cache.CacheManager
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.GenericContainer

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Testcontainers
class WarehouseCacheTest {

    companion object {

        @Container
        @JvmStatic
        val redis =
            GenericContainer("redis:7-alpine")
                .withExposedPorts(6379)

        @DynamicPropertySource
        @JvmStatic
        fun redisProperties(registry: DynamicPropertyRegistry) {

            registry.add("spring.data.redis.host") {
                redis.host
            }

            registry.add("spring.data.redis.port") {
                redis.firstMappedPort
            }
        }
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var cacheManager: CacheManager

    @BeforeEach
    fun clearCache() {

        cacheManager.cacheNames.forEach {
            cacheManager.getCache(it)?.clear()
        }
    }

    @Test
    fun `repeated warehouse list request uses cache`() {

        mockMvc.perform(get("/warehouses"))
            .andExpect(status().isOk)

        mockMvc.perform(get("/warehouses"))
            .andExpect(status().isOk)

        val cache =
            cacheManager.getCache("warehouses")

        assertNotNull(
            cache?.get(SimpleKey.EMPTY)
        )
    }

    @Test
    fun `creating warehouse invalidates cache`() {

        mockMvc.perform(get("/warehouses"))
            .andExpect(status().isOk)

        assertNotNull(
            cacheManager.getCache("warehouses")
                ?.get(SimpleKey.EMPTY)
        )

        mockMvc.perform(
            post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "name": "New Warehouse"
                    }
                    """.trimIndent()
                )
        ).andExpect(status().isCreated)

        assertNull(
            cacheManager.getCache("warehouses")
                ?.get(SimpleKey.EMPTY)
        )
    }
}