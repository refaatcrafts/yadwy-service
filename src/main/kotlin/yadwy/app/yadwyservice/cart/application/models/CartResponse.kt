package yadwy.app.yadwyservice.cart.application.models

import java.math.BigDecimal

data class CartResponse(
    val id: Long,
    val accountId: Long,
    val items: List<CartItemResponse>,
    val total: BigDecimal
)

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)
