package yadwy.app.yadwyservice.order.domain.contracts

import yadwy.app.yadwyservice.order.domain.models.Order

/**
 * Repository contract for Order aggregate persistence.
 */
interface OrderRepository {
    /**
     * Saves an order and returns the saved order with generated IDs.
     */
    fun save(order: Order): Order

    /**
     * Finds an order by its ID.
     */
    fun findById(orderId: Long): Order?

    /**
     * Finds all orders for a customer.
     */
    fun findByAccountId(accountId: Long): List<Order>

    /**
     * Finds all orders containing products from a specific seller.
     */
    fun findBySellerId(sellerId: Long): List<Order>

    /**
     * Checks if an order exists by ID.
     */
    fun existsById(orderId: Long): Boolean
}
