package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

data class OrderLine(
    val productId: Long,
    val productName: Localized,
    val unitPrice: Amount,
    val quantity: Quantity
) : ValueObject {

    init {
        require(quantity.value >= 1) { "Quantity must be at least 1" }
    }

    fun calculateSubtotal(): Amount = unitPrice * quantity
}
