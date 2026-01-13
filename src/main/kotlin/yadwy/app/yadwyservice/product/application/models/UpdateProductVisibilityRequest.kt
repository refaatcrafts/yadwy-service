package yadwy.app.yadwyservice.product.application.models

data class UpdateProductVisibilityRequest(
    val productId: Long,
    val sellerId: Long,
    val visible: Boolean
)
