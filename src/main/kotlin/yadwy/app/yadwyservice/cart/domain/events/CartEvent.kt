package yadwy.app.yadwyservice.cart.domain.events

import yadwy.app.yadwyservice.cart.domain.models.CartId
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent
import java.math.BigDecimal

sealed interface CartEvent : DomainEvent

data class CartCreatedEvent(
    val cartId: CartId,
    val accountId: Long
) : CartEvent

data class ItemAddedToCartEvent(
    val cartId: CartId,
    val productId: Long,
    val quantity: Int,
    val unitPrice: BigDecimal
) : CartEvent

data class ItemRemovedFromCartEvent(
    val cartId: CartId,
    val productId: Long
) : CartEvent

data class CartClearedEvent(
    val cartId: CartId
) : CartEvent
