package yadwy.app.yadwyservice.order.domain.contracts

import yadwy.app.yadwyservice.order.domain.models.CartItem

interface CartGateway {
    fun getCartItems(accountId: Long): List<CartItem>
    fun clearCart(accountId: Long)
}
