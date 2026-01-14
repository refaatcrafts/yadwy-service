package yadwy.app.yadwyservice.cart.domain.models

import yadwy.app.yadwyservice.cart.domain.events.*
import yadwy.app.yadwyservice.cart.domain.exceptions.CartItemNotFoundException
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot
import java.math.BigDecimal

class Cart internal constructor(
    private val cartId: CartId,
    private val accountId: Long,
    private val items: MutableList<CartItem>
) : AggregateRoot<CartId, CartEvent>(id = cartId) {

    companion object {
        fun create(accountId: Long): Cart {
            val cart = Cart(
                cartId = CartId(0),
                accountId = accountId,
                items = mutableListOf()
            )
            cart.raiseEvent(CartCreatedEvent(cart.cartId, accountId))
            return cart
        }

        fun reconstitute(
            cartId: CartId,
            accountId: Long,
            items: MutableList<CartItem>
        ): Cart = Cart(cartId, accountId, items)
    }

    fun addItem(productId: Long, quantity: Int, unitPrice: BigDecimal) {
        require(quantity >= 1) { "Quantity must be at least 1" }
        require(unitPrice >= BigDecimal.ZERO) { "Unit price cannot be negative" }

        val existingItem = items.find { it.getProductId() == productId }
        if (existingItem != null) {
            existingItem.increaseQuantity(quantity)
        } else {
            items.add(CartItem.create(productId, quantity, unitPrice))
        }
        raiseEvent(ItemAddedToCartEvent(cartId, productId, quantity, unitPrice))
    }

    fun removeItem(productId: Long) {
        val item = items.find { it.getProductId() == productId }
            ?: throw CartItemNotFoundException(productId)
        items.remove(item)
        raiseEvent(ItemRemovedFromCartEvent(cartId, productId))
    }

    fun updateItemQuantity(productId: Long, quantity: Int) {
        if (quantity == 0) {
            removeItem(productId)
            return
        }

        val item = items.find { it.getProductId() == productId }
            ?: throw CartItemNotFoundException(productId)
        item.updateQuantity(quantity)
        raiseEvent(ItemAddedToCartEvent(cartId, productId, quantity, item.getUnitPrice()))
    }

    fun clear() {
        items.clear()
        raiseEvent(CartClearedEvent(cartId))
    }

    fun calculateTotal(): BigDecimal {
        return items.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.getSubtotal()) }
    }

    fun getId() = cartId
    fun getAccountId() = accountId
    fun getItems(): List<CartItem> = items.toList()
}
