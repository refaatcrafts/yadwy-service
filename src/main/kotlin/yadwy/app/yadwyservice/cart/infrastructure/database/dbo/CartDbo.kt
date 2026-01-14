package yadwy.app.yadwyservice.cart.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("carts", schema = "cart")
data class CartDbo(
    @Id
    val id: Long? = null,
    val accountId: Long,
    @MappedCollection(idColumn = "cart_id")
    val items: Set<CartItemDbo> = emptySet()
)
