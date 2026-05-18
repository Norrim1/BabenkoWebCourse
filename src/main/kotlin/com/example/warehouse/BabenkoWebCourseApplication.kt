package com.example.warehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class BabenkoWebCourseApplication

fun main(args: Array<String>) {
    runApplication<BabenkoWebCourseApplication>(*args)
}
