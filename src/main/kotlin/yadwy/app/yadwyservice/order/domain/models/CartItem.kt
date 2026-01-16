package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

data class CartItem(
    val productId: Long,
    val sellerId: Long,
    val productName: Localized,
    val unitPrice: Amount,
    val quantity: Quantity,
    val stock: Int
) : ValueObject {

    fun toOrderLine(): OrderLine = OrderLine(
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity
    )

    fun hasSufficientStock(): Boolean = quantity.value <= stock
}
