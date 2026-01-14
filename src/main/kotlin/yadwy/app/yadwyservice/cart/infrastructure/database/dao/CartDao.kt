package yadwy.app.yadwyservice.cart.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartDbo

interface CartDao : ListCrudRepository<CartDbo, Long> {
    fun findByAccountId(accountId: Long): CartDbo?
    fun existsByAccountId(accountId: Long): Boolean
}
