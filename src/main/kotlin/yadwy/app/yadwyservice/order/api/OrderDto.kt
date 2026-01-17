package yadwy.app.yadwyservice.order.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.time.Instant
data class OrderDto(
    val id: Long,
    val accountId: Long,
    val status: String,
    val sellerOrders: List<SellerOrderDto>,
    val total: Amount,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class SellerOrderDto(
    val id: Long,
    val sellerId: Long,
    val status: String,
    val orderLines: List<OrderLineDto>,
    val subtotal: Amount,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class OrderLineDto(
    val productId: Long,
    val productName: Localized,
    val unitPrice: Amount,
    val quantity: Int,
    val subtotal: Amount
)
