package yadwy.app.yadwyservice.order.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

data class ShippingAddress(
    val recipientName: String,
    val street: String,
    val city: String,
    val governorate: String,
    val phone: String,
    val notes: String? = null
) : ValueObject {

    init {
        require(recipientName.isNotBlank()) { "Recipient name cannot be blank" }
        require(street.isNotBlank()) { "Street cannot be blank" }
        require(city.isNotBlank()) { "City cannot be blank" }
        require(governorate.isNotBlank()) { "Governorate cannot be blank" }
        require(phone.isNotBlank()) { "Phone cannot be blank" }
    }
}
