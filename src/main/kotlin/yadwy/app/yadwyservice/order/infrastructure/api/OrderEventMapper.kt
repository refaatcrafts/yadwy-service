package yadwy.app.yadwyservice.order.infrastructure.api

import yadwy.app.yadwyservice.order.api.OrderPlacedIntegrationEvent
import yadwy.app.yadwyservice.order.api.OrderPlacedIntegrationEvent.OrderLineItem
import yadwy.app.yadwyservice.order.domain.events.OrderConfirmedEvent
import yadwy.app.yadwyservice.order.domain.events.OrderEvent
import yadwy.app.yadwyservice.order.domain.events.OrderReceivedEvent
import yadwy.app.yadwyservice.order.domain.events.SellerOrderReceivedEvent
import yadwy.app.yadwyservice.order.domain.models.Order
import yadwy.app.yadwyservice.order.domain.models.OrderId
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

fun OrderEvent.toIntegrationEvent(order: Order, persistedOrderId: OrderId): IntegrationEvent? {
    return when (this) {
        is OrderReceivedEvent -> OrderPlacedIntegrationEvent(
            orderId = persistedOrderId.value,
            lineItems = order.getAllOrderLines().map { line ->
                OrderLineItem(
                    productId = line.productId,
                    quantity = line.quantity.value
                )
            }
        )
        is SellerOrderReceivedEvent -> null // Internal event, no cross-module publishing needed
        is OrderConfirmedEvent -> null // Reserved for future use
    }
}
