package yadwy.app.yadwyservice.product.api

import java.math.BigDecimal

interface ProductAPI {
    fun existsById(productId: Long): Boolean
    fun getPrice(productId: Long): BigDecimal?
    fun getStock(productId: Long): Int?
}
