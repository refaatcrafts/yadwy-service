package yadwy.app.yadwyservice.cart.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class CartItemId(val id: Long) : ValueObject
