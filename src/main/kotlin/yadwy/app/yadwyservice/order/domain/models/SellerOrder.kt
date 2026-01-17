package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.Entity
import java.time.Instant

class SellerOrder internal constructor(
    private val sellerOrderId: SellerOrderId,
    private val sellerId: Long,
    private val orderLines: List<OrderLine>,
    private var status: SellerOrderStatus,
    private val createdAt: Instant,
    private var updatedAt: Instant
) : Entity<SellerOrderId>(sellerOrderId) {

    init {
        require(orderLines.isNotEmpty()) { "SellerOrder must have at least one order line" }
    }

    companion object {
        fun create(
            sellerId: Long,
            orderLines: List<OrderLine>
        ): SellerOrder {
            val now = Instant.now()
            return SellerOrder(
                sellerOrderId = SellerOrderId(0),
                sellerId = sellerId,
                orderLines = orderLines,
                status = SellerOrderStatus.RECEIVED,
                createdAt = now,
                updatedAt = now
            )
        }

        fun reconstitute(
            sellerOrderId: SellerOrderId,
            sellerId: Long,
            orderLines: List<OrderLine>,
            status: SellerOrderStatus,
            createdAt: Instant,
            updatedAt: Instant
        ): SellerOrder = SellerOrder(
            sellerOrderId,
            sellerId,
            orderLines,
            status,
            createdAt,
            updatedAt
        )
    }

    fun calculateSubtotal(): Amount {
        return orderLines.fold(Amount.ZERO) { acc, line -> acc + line.calculateSubtotal() }
    }

    fun getId() = sellerOrderId
    fun getSellerId() = sellerId
    fun getOrderLines(): List<OrderLine> = orderLines
    fun getStatus() = status
    fun getCreatedAt() = createdAt
    fun getUpdatedAt() = updatedAt
}
