package yadwy.app.yadwyservice.cart.domain.contracts

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

interface ProductGateway {
    fun productExists(productId: Long): Boolean
    fun getProductPrice(productId: Long): Amount?
    fun getAvailableStock(productId: Long): Int?
}
