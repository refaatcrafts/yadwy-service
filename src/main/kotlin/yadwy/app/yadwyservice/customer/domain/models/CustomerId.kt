package yadwy.app.yadwyservice.customer.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class CustomerId(val id: Long) : ValueObject
