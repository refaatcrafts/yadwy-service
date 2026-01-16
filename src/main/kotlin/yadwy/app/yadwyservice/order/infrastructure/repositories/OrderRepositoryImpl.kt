package yadwy.app.yadwyservice.order.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.order.domain.models.*
import yadwy.app.yadwyservice.order.infrastructure.database.dao.OrderDao
import yadwy.app.yadwyservice.order.infrastructure.database.dbo.OrderDbo
import yadwy.app.yadwyservice.order.infrastructure.database.dbo.OrderLineDbo
import yadwy.app.yadwyservice.order.infrastructure.database.dbo.SellerOrderDbo
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity
import java.time.Instant

@Component
class OrderRepositoryImpl(
    private val orderDao: OrderDao,
) : OrderRepository {

    override fun save(order: Order): Order {
        val orderDbo = order.toDbo()
        val savedOrder = orderDao.save(orderDbo)

        return savedOrder.toDomain()
    }

    override fun findById(orderId: Long): Order? {
        return orderDao.findById(orderId).orElse(null)?.toDomain()
    }

    override fun findByAccountId(accountId: Long): List<Order> {
        return orderDao.findByAccountId(accountId).map { it.toDomain() }
    }

    override fun findBySellerId(sellerId: Long): List<Order> {
        return orderDao.findBySellerId(sellerId).map { it.toDomain() }
    }

    override fun existsById(orderId: Long): Boolean {
        return orderDao.existsById(orderId)
    }

    private fun Order.toDbo() = OrderDbo(
        id = if (getId().value == 0L) null else getId().value,
        accountId = getAccountId(),
        status = getStatus().name,
        createdAt = getCreatedAt(),
        updatedAt = getUpdatedAt(),
        sellerOrders = getSellerOrders().map { it.toDbo() }.toSet()
    )

    private fun SellerOrder.toDbo() = SellerOrderDbo(
        id = if (getId().value == 0L) null else getId().value,
        sellerId = getSellerId(),
        status = getStatus().name,
        createdAt = getCreatedAt(),
        updatedAt = getUpdatedAt(),
        orderLines = getOrderLines().map { it.toDbo() }.toSet()
    )

    private fun OrderLine.toDbo(): OrderLineDbo {
        val now = Instant.now()
        return OrderLineDbo(
            id = null,
            productId = productId,
            productName = productName,
            unitPrice = unitPrice.value,
            quantity = quantity.value,
            createdAt = now,
            updatedAt = now
        )
    }

    // DBO to Domain mapping
    private fun OrderDbo.toDomain() = Order.reconstitute(
        orderId = OrderId(id!!),
        accountId = accountId,
        sellerOrders = sellerOrders.map { it.toDomain() },
        status = OrderStatus.valueOf(status),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun SellerOrderDbo.toDomain() = SellerOrder.reconstitute(
        sellerOrderId = SellerOrderId(id!!),
        sellerId = sellerId,
        orderLines = orderLines.map { it.toDomain() },
        status = SellerOrderStatus.valueOf(status),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun OrderLineDbo.toDomain() = OrderLine(
        productId = productId,
        productName = productName,
        unitPrice = Amount.of(unitPrice),
        quantity = Quantity.of(quantity)
    )
}
