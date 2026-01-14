package yadwy.app.yadwyservice.cart.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.Entity

class CartItem internal constructor(
    private val cartItemId: CartItemId,
    private val productId: Long,
    private var quantity: Quantity,
    private val unitPrice: Amount
) : Entity<CartItemId>(cartItemId) {

    companion object {
        fun create(
            productId: Long,
            quantity: Quantity,
            unitPrice: Amount
        ): CartItem {
            return CartItem(
                cartItemId = CartItemId(0),
                productId = productId,
                quantity = quantity,
                unitPrice = unitPrice
            )
        }

        fun reconstitute(
            cartItemId: CartItemId,
            productId: Long,
            quantity: Quantity,
            unitPrice: Amount
        ): CartItem = CartItem(cartItemId, productId, quantity, unitPrice)
    }

    fun updateQuantity(newQuantity: Quantity) {
        this.quantity = newQuantity
    }

    fun increaseQuantity(amount: Quantity) {
        this.quantity = this.quantity + amount
    }

    fun getSubtotal(): Amount = unitPrice * quantity

    fun getId() = cartItemId
    fun getProductId() = productId
    fun getQuantity() = quantity
    fun getUnitPrice() = unitPrice
}
