package yadwy.app.yadwyservice.cart.domain.exceptions

class CartNotFoundException(accountId: Long) :
    RuntimeException("Cart not found for account: $accountId")

class CartItemNotFoundException(productId: Long) :
    RuntimeException("Cart item not found for product: $productId")

class ProductNotFoundException(productId: Long) :
    RuntimeException("Product not found: $productId")

class InsufficientStockException(productId: Long, requested: Int, available: Int) :
    RuntimeException("Insufficient stock for product $productId: requested $requested, available $available")

class InvalidQuantityException(quantity: Int) :
    RuntimeException("Invalid quantity: $quantity. Must be at least 1")
