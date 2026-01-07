package yadwy.app.yadwyservice.sharedkernel.api

import app.yadwy.model.ApiError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yadwy.app.yadwyservice.identity.domain.exceptions.AccountAlreadyExistsException
import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidCredentialsException
import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidTokenException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(AccountAlreadyExistsException::class)
    fun handleAccountAlreadyExists(ex: AccountAlreadyExistsException): ResponseEntity<ApiError> {
        logger.warn("Account already exists: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiError(code = "ACCOUNT_ALREADY_EXISTS", message = ex.message ?: "Account already exists"))
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(ex: InvalidCredentialsException): ResponseEntity<ApiError> {
        logger.warn("Invalid credentials attempt")
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiError(code = "INVALID_CREDENTIALS", message = ex.message ?: "Invalid credentials"))
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(ex: InvalidTokenException): ResponseEntity<ApiError> {
        logger.warn("Invalid token: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiError(code = "INVALID_TOKEN", message = ex.message ?: "Invalid token"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiError> {
        logger.warn("Validation error: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError(code = "VALIDATION_ERROR", message = ex.message ?: "Invalid request"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiError> {
        logger.error("Unexpected error", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError(code = "INTERNAL_ERROR", message = "An unexpected error occurred"))
    }
}
