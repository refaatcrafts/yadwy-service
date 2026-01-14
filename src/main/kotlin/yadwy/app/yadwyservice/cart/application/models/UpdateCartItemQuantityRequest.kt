package yadwy.app.yadwyservice.cart.application.models

data class UpdateCartItemQuantityRequest(
    val accountId: Long,
    val productId: Long,
    val quantity: Int
)
