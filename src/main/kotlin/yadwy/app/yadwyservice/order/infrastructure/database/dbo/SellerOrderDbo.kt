package yadwy.app.yadwyservice.order.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

/**
 * Database object for seller orders (embedded entity within Order aggregate).
 */
@Table("seller_orders", schema = "order")
data class SellerOrderDbo(
    @Id
    val id: Long? = null,
    val sellerId: Long,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    @MappedCollection(idColumn = "seller_order_id")
    val orderLines: Set<OrderLineDbo> = emptySet()
)
