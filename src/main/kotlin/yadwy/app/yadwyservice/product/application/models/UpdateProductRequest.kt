package yadwy.app.yadwyservice.product.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.math.BigDecimal

data class UpdateProductRequest(
    val productId: Long,
    val sellerId: Long,
    val name: Localized? = null,
    val description: Localized? = null,
    val images: List<String>? = null,
    val price: BigDecimal? = null,
    val compareAtPrice: BigDecimal? = null,
    val stock: Int? = null,
    val trackInventory: Boolean? = null,
    val visible: Boolean? = null
)
