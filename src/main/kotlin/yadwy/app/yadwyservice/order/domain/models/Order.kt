package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.order.domain.events.OrderEvent
import yadwy.app.yadwyservice.order.domain.events.OrderReceivedEvent
import yadwy.app.yadwyservice.order.domain.events.SellerOrderReceivedEvent
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot
import java.time.Instant

class Order internal constructor(
    private val orderId: OrderId,
    private val accountId: Long,
    private val sellerOrders: List<SellerOrder>,
    private var status: OrderStatus,
    private val createdAt: Instant,
    private var updatedAt: Instant
) : AggregateRoot<OrderId, OrderEvent>(id = orderId) {

    init {
        require(sellerOrders.isNotEmpty()) { "Order must have at least one SellerOrder" }
    }

    companion object {
        fun create(accountId: Long, cartItems: List<CartItem>): Order {
            require(cartItems.isNotEmpty()) { "Cart cannot be empty" }

            val orderLinesBySeller = cartItems
                .groupBy { it.sellerId }
                .mapValues { (_, items) ->
                    items.map { it.toOrderLine() }
                }

            val sellerOrders = orderLinesBySeller.map { (sellerId, orderLines) ->
                SellerOrder.create(sellerId, orderLines)
            }

            val now = Instant.now()
            val order = Order(
                orderId = OrderId(0),
                accountId = accountId,
                sellerOrders = sellerOrders,
                status = OrderStatus.RECEIVED,
                createdAt = now,
                updatedAt = now
            )

            // Raise OrderReceivedEvent for customer notification
            order.raiseEvent(
                OrderReceivedEvent(
                    orderId = order.orderId,
                    accountId = accountId,
                    sellerIds = orderLinesBySeller.keys.toList(),
                    total = order.calculateTotal()
                )
            )

            // Raise SellerOrderReceivedEvent for each seller
            sellerOrders.forEach { sellerOrder ->
                order.raiseEvent(
                    SellerOrderReceivedEvent(
                        orderId = order.orderId,
                        sellerId = sellerOrder.getSellerId(),
                        sellerOrderId = sellerOrder.getId(),
                        subtotal = sellerOrder.calculateSubtotal()
                    )
                )
            }

            return order
        }

        fun reconstitute(
            orderId: OrderId,
            accountId: Long,
            sellerOrders: List<SellerOrder>,
            status: OrderStatus,
            createdAt: Instant,
            updatedAt: Instant
        ): Order = Order(orderId, accountId, sellerOrders, status, createdAt, updatedAt)
    }

    fun getAllOrderLines(): List<OrderLine> = sellerOrders.flatMap { it.getOrderLines() }

    fun getOrderLineCount(): Int = sellerOrders.sumOf { so -> so.getOrderLines().sumOf { it.quantity.value } }

    fun calculateTotal(): Amount {
        return sellerOrders.fold(Amount.ZERO) { acc, sellerOrder ->
            acc + sellerOrder.calculateSubtotal()
        }
    }

    fun getSellerOrder(sellerId: Long): SellerOrder? {
        return sellerOrders.find { it.getSellerId() == sellerId }
    }

    fun getId() = orderId
    fun getAccountId() = accountId
    fun getSellerOrders(): List<SellerOrder> = sellerOrders
    fun getStatus() = status
    fun getCreatedAt() = createdAt
    fun getUpdatedAt() = updatedAt
}
