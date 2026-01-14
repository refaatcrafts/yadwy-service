package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.model.CartItemResponseDto
import app.yadwy.model.CartResponseDto
import yadwy.app.yadwyservice.cart.application.models.CartItemResponse
import yadwy.app.yadwyservice.cart.application.models.CartResponse

fun CartResponse.toDto() = CartResponseDto(
    id = id,
    accountId = accountId,
    items = items.map { it.toDto() },
    total = total.value.toDouble()
)

fun CartItemResponse.toDto() = CartItemResponseDto(
    id = id,
    productId = productId,
    quantity = quantity,
    unitPrice = unitPrice.value.toDouble(),
    subtotal = subtotal.value.toDouble()
)
