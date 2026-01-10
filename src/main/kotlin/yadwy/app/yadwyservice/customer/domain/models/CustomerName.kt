package yadwy.app.yadwyservice.customer.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class CustomerName(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Customer name cannot be blank" }
        require(value.length <= 100) { "Customer name cannot exceed 100 characters" }
    }
}
