package yadwy.app.yadwyservice.order.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("orders", schema = "order")
data class OrderDbo(
    @Id
    val id: Long? = null,
    val accountId: Long,
    val status: String,
    val customerName: String,
    val address: String,
    val province: String,
    val phone: String,
    val notes: String?,
    val paymentMethod: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    @MappedCollection(idColumn = "order_id")
    val sellerOrders: Set<SellerOrderDbo> = emptySet()
)
