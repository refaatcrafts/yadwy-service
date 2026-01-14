package yadwy.app.yadwyservice.product.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.models.Product
import yadwy.app.yadwyservice.product.domain.models.ProductId
import yadwy.app.yadwyservice.product.infrastructure.database.dao.ProductDao
import yadwy.app.yadwyservice.product.infrastructure.database.dbo.ProductDbo
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Component
class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {

    override fun save(product: Product): Product {
        val dbo = ProductDbo(
            id = if (product.getId().id == 0L) null else product.getId().id,
            sellerId = product.getSellerId(),
            name = product.getName(),
            description = product.getDescription(),
            images = product.getImages().toTypedArray(),
            price = product.getPrice().value,
            compareAtPrice = product.getCompareAtPrice()?.value,
            categoryId = product.getCategoryId(),
            stock = product.getStock(),
            trackInventory = product.isTrackInventory(),
            visible = product.isVisible()
        )
        val saved = productDao.save(dbo)
        return saved.toDomain()
    }

    override fun findById(productId: Long): Product? {
        return productDao.findById(productId).orElse(null)?.toDomain()
    }

    override fun findBySellerId(sellerId: Long): List<Product> {
        return productDao.findBySellerId(sellerId).map { it.toDomain() }
    }

    override fun findByCategoryId(categoryId: Long): List<Product> {
        return productDao.findByCategoryId(categoryId).map { it.toDomain() }
    }

    override fun findVisibleProducts(): List<Product> {
        return productDao.findByVisibleTrue().map { it.toDomain() }
    }

    override fun findVisibleByCategoryId(categoryId: Long): List<Product> {
        return productDao.findByCategoryIdAndVisibleTrue(categoryId).map { it.toDomain() }
    }

    override fun delete(productId: Long) {
        productDao.deleteById(productId)
    }

    override fun existsById(productId: Long): Boolean {
        return productDao.existsById(productId)
    }

    private fun ProductDbo.toDomain(): Product = Product.reconstitute(
        productId = ProductId(id!!),
        sellerId = sellerId,
        name = name,
        description = description,
        images = images.toList(),
        price = Amount.of(price),
        compareAtPrice = compareAtPrice?.let { Amount.of(it) },
        categoryId = categoryId,
        stock = stock,
        trackInventory = trackInventory,
        visible = visible
    )
}
