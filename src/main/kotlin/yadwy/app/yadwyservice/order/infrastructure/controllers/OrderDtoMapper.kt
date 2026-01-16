package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.model.LocalizedDto
import app.yadwy.model.OrderLineResponseDto
import app.yadwy.model.OrderResponseDto
import app.yadwy.model.SellerOrderResponseDto
import yadwy.app.yadwyservice.order.application.models.OrderLineResponse
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.application.models.SellerOrderResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

fun OrderResponse.toDto() = OrderResponseDto(
    id = id,
    accountId = accountId,
    status = OrderResponseDto.Status.forValue(status),
    sellerOrders = sellerOrders.map { it.toDto() },
    total = total.value.toDouble(),
    createdAt = LocalDateTime.ofInstant(createdAt, ZoneOffset.UTC)
)

fun SellerOrderResponse.toDto() = SellerOrderResponseDto(
    id = id,
    sellerId = sellerId,
    status = SellerOrderResponseDto.Status.forValue(status),
    lines = orderLines.map { it.toDto() },
    subtotal = subtotal.value.toDouble()
)

fun OrderLineResponse.toDto() = OrderLineResponseDto(
    productId = productId,
    productName = LocalizedDto(
        ar = productName.ar,
        en = productName.en
    ),
    unitPrice = unitPrice.value.toDouble(),
    quantity = quantity,
    subtotal = subtotal.value.toDouble()
)
