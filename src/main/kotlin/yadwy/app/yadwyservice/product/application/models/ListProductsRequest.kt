package yadwy.app.yadwyservice.product.application.models

data class ListProductsRequest(
    val sellerId: Long? = null,
    val categoryId: Long? = null,
    val visibleOnly: Boolean = false
)
