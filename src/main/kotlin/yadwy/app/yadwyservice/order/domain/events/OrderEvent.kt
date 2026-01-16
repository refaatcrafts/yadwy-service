package yadwy.app.yadwyservice.order.domain.events

import yadwy.app.yadwyservice.order.domain.models.OrderId
import yadwy.app.yadwyservice.order.domain.models.SellerOrderId
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

/**
 * Sealed interface for all Order domain events.
 */
sealed interface OrderEvent : DomainEvent

/**
 * Event raised when a new order is received.
 * Used for customer notification.
 */
data class OrderReceivedEvent(
    val orderId: OrderId,
    val accountId: Long,
    val sellerIds: List<Long>,
    val total: Amount
) : OrderEvent

/**
 * Event raised for each seller when their portion of an order is received.
 * Used for seller notification.
 */
data class SellerOrderReceivedEvent(
    val orderId: OrderId,
    val sellerId: Long,
    val sellerOrderId: SellerOrderId,
    val subtotal: Amount
) : OrderEvent

/**
 * Event raised when an order is confirmed.
 * Reserved for future use.
 */
data class OrderConfirmedEvent(
    val orderId: OrderId
) : OrderEvent
