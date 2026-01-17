package yadwy.app.yadwyservice.order.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

data class OrderPlacedIntegrationEvent(
    val orderId: Long,
    val lineItems: List<OrderLineItem>
) : IntegrationEvent {

    data class OrderLineItem(
        val productId: Long,
        val quantity: Int
    )
}