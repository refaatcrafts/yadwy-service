package yadwy.app.yadwyservice.order.domain.exceptions

class OrderNotFoundException(orderId: Long) :
    RuntimeException("Order not found: $orderId")

class SellerOrderNotFoundException(orderId: Long, sellerId: Long) :
    RuntimeException("Seller order not found for order $orderId and seller $sellerId")

class EmptyOrderException :
    RuntimeException("Order must have at least one item")

class ProductNotFoundException(productId: Long) :
    RuntimeException("Product not found: $productId")

class InsufficientStockException(productId: Long, requested: Int, available: Int) :
    RuntimeException("Insufficient stock for product $productId: requested $requested, available $available")
