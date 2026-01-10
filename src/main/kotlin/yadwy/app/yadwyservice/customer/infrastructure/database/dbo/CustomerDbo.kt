package yadwy.app.yadwyservice.customer.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("customers", schema = "customer")
data class CustomerDbo(
    @Id
    val id: Long? = null,
    val accountId: Long,
    val customerName: String
)
