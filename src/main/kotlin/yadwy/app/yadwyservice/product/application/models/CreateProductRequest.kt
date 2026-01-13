package yadwy.app.yadwyservice.product.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.math.BigDecimal

data class CreateProductRequest(
    val sellerId: Long,
    val name: Localized,
    val description: Localized? = null,
    val images: List<String> = emptyList(),
    val price: BigDecimal,
    val compareAtPrice: BigDecimal? = null,
    val categoryId: Long,
    val stock: Int = 0,
    val trackInventory: Boolean = true,
    val visible: Boolean = false
)
