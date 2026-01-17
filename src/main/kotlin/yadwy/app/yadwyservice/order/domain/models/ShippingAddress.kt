package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

data class ShippingAddress(
    val customerName: String,
    val address: String,
    val province: String,
    val phone: String,
    val notes: String? = null
) : ValueObject {

    init {
        require(customerName.isNotBlank()) { "Customer name cannot be blank" }
        require(address.isNotBlank()) { "Address cannot be blank" }
        require(province.isNotBlank()) { "Province cannot be blank" }
        require(phone.isNotBlank()) { "Phone cannot be blank" }
    }
}
