package yadwy.app.yadwyservice.product.application.models

data class DeleteProductRequest(
    val productId: Long,
    val sellerId: Long
)
