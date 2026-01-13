package yadwy.app.yadwyservice.product.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val sellerId: Long,
    val name: Localized,
    val description: Localized?,
    val images: List<String>,
    val price: BigDecimal,
    val compareAtPrice: BigDecimal?,
    val categoryId: Long,
    val stock: Int,
    val trackInventory: Boolean,
    val visible: Boolean
)
