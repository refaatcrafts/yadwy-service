package yadwy.app.yadwyservice.cart.application.models

data class AddItemToCartRequest(
    val accountId: Long,
    val productId: Long,
    val quantity: Int
)
