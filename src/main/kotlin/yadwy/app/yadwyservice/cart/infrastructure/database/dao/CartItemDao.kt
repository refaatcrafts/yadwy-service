package yadwy.app.yadwyservice.cart.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartItemDbo

interface CartItemDao : ListCrudRepository<CartItemDbo, Long> {
    fun findByCartId(cartId: Long): List<CartItemDbo>
    fun deleteByCartId(cartId: Long)
}
