package yadwy.app.yadwyservice.product.domain.events

import yadwy.app.yadwyservice.product.domain.models.ProductId
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface ProductEvent : DomainEvent

data class ProductCreatedEvent(
    val productId: ProductId,
    val sellerId: Long,
    val name: Localized,
    val categoryId: Long
) : ProductEvent

data class ProductUpdatedEvent(
    val productId: ProductId
) : ProductEvent

data class ProductDeletedEvent(
    val productId: ProductId,
    val sellerId: Long
) : ProductEvent

data class ProductVisibilityChangedEvent(
    val productId: ProductId,
    val visible: Boolean
) : ProductEvent

data class ProductStockChangedEvent(
    val productId: ProductId,
    val previousStock: Int,
    val newStock: Int
) : ProductEvent
