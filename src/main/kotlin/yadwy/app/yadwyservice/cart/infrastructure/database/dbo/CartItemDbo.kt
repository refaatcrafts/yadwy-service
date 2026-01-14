package yadwy.app.yadwyservice.cart.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("cart_items", schema = "cart")
data class CartItemDbo(
    @Id
    val id: Long? = null,
    val productId: Long,
    val quantity: Int,
    val unitPrice: BigDecimal
)
