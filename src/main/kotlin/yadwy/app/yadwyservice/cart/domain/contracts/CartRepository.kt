package yadwy.app.yadwyservice.cart.domain.contracts

import yadwy.app.yadwyservice.cart.domain.models.Cart

interface CartRepository {
    fun save(cart: Cart): Cart
    fun findByAccountId(accountId: Long): Cart?
    fun findById(cartId: Long): Cart?
    fun existsByAccountId(accountId: Long): Boolean
}
