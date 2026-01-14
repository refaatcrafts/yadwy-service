package yadwy.app.yadwyservice.cart.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.Entity
import java.math.BigDecimal

class CartItem internal constructor(
    private val cartItemId: CartItemId,
    private val productId: Long,
    private var quantity: Int,
    private val unitPrice: BigDecimal
) : Entity<CartItemId>(cartItemId) {

    init {
        require(quantity >= 1) { "Quantity must be at least 1" }
        require(unitPrice >= BigDecimal.ZERO) { "Unit price cannot be negative" }
    }

    companion object {
        fun create(
            productId: Long,
            quantity: Int,
            unitPrice: BigDecimal
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
            quantity: Int,
            unitPrice: BigDecimal
        ): CartItem = CartItem(cartItemId, productId, quantity, unitPrice)
    }

    fun updateQuantity(newQuantity: Int) {
        require(newQuantity >= 1) { "Quantity must be at least 1" }
        this.quantity = newQuantity
    }

    fun increaseQuantity(amount: Int) {
        require(amount >= 1) { "Amount must be at least 1" }
        this.quantity += amount
    }

    fun getSubtotal(): BigDecimal = unitPrice.multiply(BigDecimal(quantity))

    fun getId() = cartItemId
    fun getProductId() = productId
    fun getQuantity() = quantity
    fun getUnitPrice() = unitPrice
}
