package yadwy.app.yadwyservice.seller.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class StoreName(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Store name cannot be blank" }
        require(value.length <= 100) { "Store name cannot exceed 100 characters" }
    }
}
