package yadwy.app.yadwyservice.seller.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sellers", schema = "seller")
data class SellerDbo(
    @Id
    val id: Long? = null,
    val accountId: Long,
    val storeName: String
)
