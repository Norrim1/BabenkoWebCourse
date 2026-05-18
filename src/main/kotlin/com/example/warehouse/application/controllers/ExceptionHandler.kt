package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.error.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import com.example.warehouse.domain.exceptions.BadRequestException
import com.example.warehouse.domain.exceptions.ConflictException
import com.example.warehouse.domain.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NotFoundException): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = 404,
            error = "Not Found",
            message = ex.message ?: ""
        )
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: BadRequestException): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = 400,
            error = "Bad Request",
            message = ex.message ?: ""
        )
    }

    @ExceptionHandler(ConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(ex: ConflictException): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = 409,
            error = "Conflict",
            message = ex.message ?: ""
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleOther(ex: Exception): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = 500,
            error = "Internal Server Error",
            message = ex.message ?: "Unexpected error"
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(ex: MethodArgumentNotValidException): ErrorResponse {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = 400,
            error = "Validation Error",
            message = message
        )
    }
}