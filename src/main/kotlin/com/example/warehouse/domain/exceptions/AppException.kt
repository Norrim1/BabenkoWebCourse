package com.example.warehouse.domain.exceptions

sealed class AppException(message: String) : RuntimeException(message)