package com.example.warehouse.application.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Suppress("DEPRECATION")
@Configuration
class CacheConfig {

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
        val mapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType(Any::class.java)
                    .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
            )
        }

        val serializer = GenericJackson2JsonRedisSerializer(mapper)
        val serializationPair = RedisSerializationContext.SerializationPair
            .fromSerializer(serializer)

        fun config(ttl: Duration) = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(ttl)
            .serializeValuesWith(serializationPair)
            .disableCachingNullValues()

        return RedisCacheManagerBuilderCustomizer { builder ->
            builder
                .withCacheConfiguration("restaurants", config(Duration.ofHours(1)))
                .withCacheConfiguration("dishes", config(Duration.ofHours(1)))
                .cacheDefaults(config(Duration.ofMinutes(5)))
        }
    }
}