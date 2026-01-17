package yadwy.app.yadwyservice.order.application.models

import yadwy.app.yadwyservice.order.domain.models.OrderSource
import yadwy.app.yadwyservice.order.domain.models.PaymentMethod
import yadwy.app.yadwyservice.order.domain.models.ShippingAddress


data class PlaceOrderRequest(
    val accountId: Long,
    val source: OrderSource,
    val items: List<OrderItemRequest>,
    val shippingAddress: ShippingAddress,
    val paymentMethod: PaymentMethod
)

data class OrderItemRequest(
    val productId: Long,
    val sellerId: Long,
    val quantity: Int
)
