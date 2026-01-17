package yadwy.app.yadwyservice.order.infrastructure.database.dao

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.order.infrastructure.database.dbo.OrderDbo

/**
 * Data Access Object for Order aggregate.
 * Only the aggregate root has a DAO - Spring Data JDBC manages the entire aggregate.
 */
interface OrderDao : ListCrudRepository<OrderDbo, Long> {
    
    /**
     * Finds all orders for a customer.
     */
    fun findByAccountId(accountId: Long): List<OrderDbo>

    /**
     * Finds all orders containing products from a specific seller.
     */
    @Query(
        """
        SELECT DISTINCT o.* FROM "order".orders o
        JOIN "order".seller_orders so ON so.order_id = o.id
        WHERE so.seller_id = :sellerId
        """
    )
    fun findBySellerId(sellerId: Long): List<OrderDbo>
}
