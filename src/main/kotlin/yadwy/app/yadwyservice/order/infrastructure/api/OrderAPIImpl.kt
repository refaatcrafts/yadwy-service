package yadwy.app.yadwyservice.order.infrastructure.api

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.api.OrderAPI
import yadwy.app.yadwyservice.order.api.OrderDto
import yadwy.app.yadwyservice.order.api.OrderLineDto
import yadwy.app.yadwyservice.order.api.SellerOrderDto
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.order.domain.models.Order
import yadwy.app.yadwyservice.order.domain.models.OrderLine
import yadwy.app.yadwyservice.order.domain.models.SellerOrder

@Service
class OrderAPIImpl(
    private val orderRepository: OrderRepository
) : OrderAPI {

    override fun getOrderById(orderId: Long): OrderDto? {
        return orderRepository.findById(orderId)?.toDto()
    }

    override fun getOrdersByAccountId(accountId: Long): List<OrderDto> {
        return orderRepository.findByAccountId(accountId).map { it.toDto() }
    }

    override fun getOrdersBySellerId(sellerId: Long): List<OrderDto> {
        return orderRepository.findBySellerId(sellerId).map { it.toDto() }
    }

    private fun Order.toDto(): OrderDto = OrderDto(
        id = getId().value,
        accountId = getAccountId(),
        status = getStatus().name,
        sellerOrders = getSellerOrders().map { it.toDto() },
        total = calculateTotal(),
        createdAt = getCreatedAt(),
        updatedAt = getUpdatedAt()
    )

    private fun SellerOrder.toDto(): SellerOrderDto = SellerOrderDto(
        id = getId().value,
        sellerId = getSellerId(),
        status = getStatus().name,
        orderLines = getOrderLines().map { it.toDto() },
        subtotal = calculateSubtotal(),
        createdAt = getCreatedAt(),
        updatedAt = getUpdatedAt()
    )

    private fun OrderLine.toDto(): OrderLineDto = OrderLineDto(
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity.value,
        subtotal = calculateSubtotal()
    )
}
