package yadwy.app.yadwyservice.cart.api

import java.math.BigDecimal

data class CartDto(
    val id: Long,
    val accountId: Long,
    val items: List<CartItemDto>,
    val total: BigDecimal
)

data class CartItemDto(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)
