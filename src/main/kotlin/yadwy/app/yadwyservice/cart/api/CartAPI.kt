package yadwy.app.yadwyservice.cart.api

interface CartAPI {
    fun getCartByAccountId(accountId: Long): CartDto?
    fun clearCart(accountId: Long)
}
