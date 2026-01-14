package yadwy.app.yadwyservice.cart.application.models

data class RemoveItemFromCartRequest(
    val accountId: Long,
    val productId: Long
)
