package yadwy.app.yadwyservice.product.domain.contracts

interface CategoryGateway {
    fun categoryExists(categoryId: Long): Boolean
}
