package yadwy.app.yadwyservice.order.application.usecases

import yadwy.app.yadwyservice.order.application.models.OrderLineResponse
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.application.models.SellerOrderResponse
import yadwy.app.yadwyservice.order.domain.models.Order
import yadwy.app.yadwyservice.order.domain.models.OrderLine
import yadwy.app.yadwyservice.order.domain.models.SellerOrder

/**
 * Extension functions for mapping domain models to response models.
 */

fun Order.toResponse() = OrderResponse(
    id = getId().value,
    accountId = getAccountId(),
    sellerOrders = getSellerOrders().map { it.toResponse() },
    shippingAddress = getShippingAddress(),
    paymentMethod = getPaymentMethod(),
    status = getStatus().name,
    total = calculateTotal(),
    createdAt = getCreatedAt(),
    updatedAt = getUpdatedAt()
)

fun SellerOrder.toResponse() = SellerOrderResponse(
    id = getId().value,
    sellerId = getSellerId(),
    orderLines = getOrderLines().map { it.toResponse() },
    status = getStatus().name,
    subtotal = calculateSubtotal(),
    createdAt = getCreatedAt(),
    updatedAt = getUpdatedAt()
)

fun OrderLine.toResponse() = OrderLineResponse(
    productId = productId,
    productName = productName,
    unitPrice = unitPrice,
    quantity = quantity.value,
    subtotal = calculateSubtotal()
)
