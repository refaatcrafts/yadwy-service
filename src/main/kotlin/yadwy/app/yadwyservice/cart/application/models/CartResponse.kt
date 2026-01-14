package yadwy.app.yadwyservice.cart.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

data class CartResponse(
    val id: Long,
    val accountId: Long,
    val items: List<CartItemResponse>,
    val total: Amount
)

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Amount,
    val subtotal: Amount
)
