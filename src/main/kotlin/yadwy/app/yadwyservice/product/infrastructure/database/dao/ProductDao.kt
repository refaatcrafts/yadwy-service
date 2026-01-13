package yadwy.app.yadwyservice.product.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.product.infrastructure.database.dbo.ProductDbo

interface ProductDao : ListCrudRepository<ProductDbo, Long> {
    fun findBySellerId(sellerId: Long): List<ProductDbo>
    fun findByCategoryId(categoryId: Long): List<ProductDbo>
    fun findByVisibleTrue(): List<ProductDbo>
    fun findByCategoryIdAndVisibleTrue(categoryId: Long): List<ProductDbo>
}
