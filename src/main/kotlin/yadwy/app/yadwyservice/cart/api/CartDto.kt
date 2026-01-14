package yadwy.app.yadwyservice.cart.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

data class CartDto(
    val id: Long,
    val accountId: Long,
    val items: List<CartItemDto>,
    val total: Amount
)

data class CartItemDto(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Amount,
    val subtotal: Amount
)
