package yadwy.app.yadwyservice.product.domain.contracts

import yadwy.app.yadwyservice.product.domain.models.Product

interface ProductRepository {
    fun save(product: Product): Product
    fun findById(productId: Long): Product?
    fun findBySellerId(sellerId: Long): List<Product>
    fun findByCategoryId(categoryId: Long): List<Product>
    fun findVisibleProducts(): List<Product>
    fun findVisibleByCategoryId(categoryId: Long): List<Product>
    fun delete(productId: Long)
    fun existsById(productId: Long): Boolean
}
