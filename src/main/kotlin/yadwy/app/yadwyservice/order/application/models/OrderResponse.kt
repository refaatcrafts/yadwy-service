package yadwy.app.yadwyservice.order.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.time.Instant

/**
 * Response model for an order.
 */
data class OrderResponse(
    val id: Long,
    val accountId: Long,
    val sellerOrders: List<SellerOrderResponse>,
    val status: String,
    val total: Amount,
    val createdAt: Instant,
    val updatedAt: Instant
)

/**
 * Response model for a seller order within an order.
 */
data class SellerOrderResponse(
    val id: Long,
    val sellerId: Long,
    val orderLines: List<OrderLineResponse>,
    val status: String,
    val subtotal: Amount,
    val createdAt: Instant,
    val updatedAt: Instant
)

/**
 * Response model for an order line.
 */
data class OrderLineResponse(
    val productId: Long,
    val productName: Localized,
    val unitPrice: Amount,
    val quantity: Int,
    val subtotal: Amount
)
