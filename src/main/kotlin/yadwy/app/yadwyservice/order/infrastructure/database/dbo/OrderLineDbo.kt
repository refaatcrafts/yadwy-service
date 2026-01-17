package yadwy.app.yadwyservice.order.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.math.BigDecimal
import java.time.Instant

/**
 * Database object for order lines (value objects within SellerOrder).
 */
@Table("order_lines", schema = "order")
data class OrderLineDbo(
    @Id
    val id: Long? = null,
    val productId: Long,
    val productName: Localized,
    val unitPrice: BigDecimal,
    val quantity: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)
