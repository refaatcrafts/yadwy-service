package yadwy.app.yadwyservice.product.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

interface ProductAPI {
    fun existsById(productId: Long): Boolean
    fun getPrice(productId: Long): Amount?
    fun getStock(productId: Long): Int?
}
