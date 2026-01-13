package yadwy.app.yadwyservice.product.domain.models

import yadwy.app.yadwyservice.product.domain.events.*
import yadwy.app.yadwyservice.product.domain.exceptions.CategoryNotFoundException
import yadwy.app.yadwyservice.product.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.product.domain.exceptions.InvalidCompareAtPriceException
import yadwy.app.yadwyservice.product.domain.exceptions.SellerNotFoundException
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot

class Product internal constructor(
    private val productId: ProductId,
    private val sellerId: Long,
    private var name: Localized,
    private var description: Localized?,
    private var images: List<String>,
    private var price: Money,
    private var compareAtPrice: Money?,
    private val categoryId: Long,
    private var stock: Int,
    private var trackInventory: Boolean,
    private var visible: Boolean
) : AggregateRoot<ProductId, ProductEvent>(id = productId) {

    companion object {
        fun create(
            sellerId: Long,
            name: Localized,
            description: Localized?,
            images: List<String>,
            price: Money,
            compareAtPrice: Money?,
            categoryId: Long,
            stock: Int,
            trackInventory: Boolean,
            visible: Boolean,
            sellerExists: (Long) -> Boolean,
            categoryExists: (Long) -> Boolean
        ): Product {
            require(stock >= 0) { "Stock cannot be negative" }

            if (!sellerExists(sellerId)) {
                throw SellerNotFoundException(sellerId)
            }

            if (!categoryExists(categoryId)) {
                throw CategoryNotFoundException(categoryId)
            }

            compareAtPrice?.let {
                if (it <= price) {
                    throw InvalidCompareAtPriceException(price.value, it.value)
                }
            }

            val product = Product(
                productId = ProductId(0),
                sellerId = sellerId,
                name = name,
                description = description,
                images = images,
                price = price,
                compareAtPrice = compareAtPrice,
                categoryId = categoryId,
                stock = stock,
                trackInventory = trackInventory,
                visible = visible
            )

            product.raiseEvent(
                ProductCreatedEvent(product.productId, sellerId, name, categoryId)
            )

            return product
        }

        fun reconstitute(
            productId: ProductId,
            sellerId: Long,
            name: Localized,
            description: Localized?,
            images: List<String>,
            price: Money,
            compareAtPrice: Money?,
            categoryId: Long,
            stock: Int,
            trackInventory: Boolean,
            visible: Boolean
        ): Product = Product(
            productId = productId,
            sellerId = sellerId,
            name = name,
            description = description,
            images = images,
            price = price,
            compareAtPrice = compareAtPrice,
            categoryId = categoryId,
            stock = stock,
            trackInventory = trackInventory,
            visible = visible
        )
    }

    fun updateDetails(
        name: Localized? = null,
        description: Localized? = null,
        images: List<String>? = null
    ) {
        name?.let { this.name = it }
        description?.let { this.description = it }
        images?.let { this.images = it }
        raiseEvent(ProductUpdatedEvent(productId))
    }

    fun updatePricing(
        price: Money? = null,
        compareAtPrice: Money? = null
    ) {
        val newPrice = price ?: this.price
        val newCompareAtPrice = compareAtPrice ?: this.compareAtPrice

        newCompareAtPrice?.let {
            if (it <= newPrice) {
                throw InvalidCompareAtPriceException(newPrice.value, it.value)
            }
        }

        price?.let { this.price = it }
        compareAtPrice?.let { this.compareAtPrice = it }
        raiseEvent(ProductUpdatedEvent(productId))
    }

    fun updateInventory(
        stock: Int? = null,
        trackInventory: Boolean? = null
    ) {
        stock?.let {
            require(it >= 0) { "Stock cannot be negative" }
            val previousStock = this.stock
            this.stock = it
            raiseEvent(ProductStockChangedEvent(productId, previousStock, it))
        }
        trackInventory?.let { this.trackInventory = it }
        raiseEvent(ProductUpdatedEvent(productId))
    }

    fun setVisibility(visible: Boolean) {
        this.visible = visible
        raiseEvent(ProductVisibilityChangedEvent(productId, visible))
    }

    fun decrementStock(quantity: Int) {
        if (!trackInventory) return

        if (quantity > stock) {
            throw InsufficientStockException(productId.id, quantity, stock)
        }

        val previousStock = stock
        stock -= quantity
        raiseEvent(ProductStockChangedEvent(productId, previousStock, stock))
    }

    fun getId() = productId
    fun getSellerId() = sellerId
    fun getName() = name
    fun getDescription() = description
    fun getImages() = images
    fun getPrice() = price
    fun getCompareAtPrice() = compareAtPrice
    fun getCategoryId() = categoryId
    fun getStock() = stock
    fun isTrackInventory() = trackInventory
    fun isVisible() = visible
}
