package yadwy.app.yadwyservice.product.domain.exceptions

import java.math.BigDecimal

class ProductNotFoundException(productId: Long) :
    RuntimeException("Product not found: $productId")

class ProductNotOwnedException(productId: Long, sellerId: Long) :
    RuntimeException("Product $productId does not belong to seller $sellerId")

class InvalidPriceException(message: String) :
    RuntimeException(message)

class InvalidCompareAtPriceException(price: BigDecimal, compareAtPrice: BigDecimal) :
    RuntimeException("Compare at price ($compareAtPrice) must be greater than price ($price)")

class InsufficientStockException(productId: Long, requested: Int, available: Int) :
    RuntimeException("Insufficient stock for product $productId: requested $requested, available $available")

class SellerNotFoundException(sellerId: Long) :
    RuntimeException("Seller not found: $sellerId")

class CategoryNotFoundException(categoryId: Long) :
    RuntimeException("Category not found: $categoryId")
