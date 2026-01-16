package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.model.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yadwy.app.yadwyservice.order.domain.exceptions.EmptyCartException
import yadwy.app.yadwyservice.order.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.order.domain.exceptions.OrderNotFoundException
import yadwy.app.yadwyservice.order.domain.exceptions.ProductNotFoundException

@RestControllerAdvice
class OrderExceptionHandler {

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(ex: OrderNotFoundException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError("ORDER_NOT_FOUND", ex.message ?: "Order not found"))
    }

    @ExceptionHandler(EmptyCartException::class)
    fun handleEmptyCart(ex: EmptyCartException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError("EMPTY_CART", ex.message ?: "Cart is empty"))
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(ex: ProductNotFoundException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError("PRODUCT_NOT_FOUND", ex.message ?: "Product not found"))
    }

    @ExceptionHandler(InsufficientStockException::class)
    fun handleInsufficientStock(ex: InsufficientStockException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError("INSUFFICIENT_STOCK", ex.message ?: "Insufficient stock"))
    }
}
