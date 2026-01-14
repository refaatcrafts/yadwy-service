package yadwy.app.yadwyservice.cart.domain.contracts

import java.math.BigDecimal

interface ProductGateway {
    fun productExists(productId: Long): Boolean
    fun getProductPrice(productId: Long): BigDecimal?
    fun getAvailableStock(productId: Long): Int?
}
