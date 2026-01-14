package yadwy.app.yadwyservice.cart.application.usecases

import yadwy.app.yadwyservice.cart.application.models.CartItemResponse
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.domain.models.Cart
import yadwy.app.yadwyservice.cart.domain.models.CartItem

fun Cart.toResponse() = CartResponse(
    id = getId().id,
    accountId = getAccountId(),
    items = getItems().map { it.toResponse() },
    total = calculateTotal()
)

fun CartItem.toResponse() = CartItemResponse(
    id = getId().id,
    productId = getProductId(),
    quantity = getQuantity().value,
    unitPrice = getUnitPrice(),
    subtotal = getSubtotal()
)
