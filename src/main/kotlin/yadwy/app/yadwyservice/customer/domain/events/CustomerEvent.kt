package yadwy.app.yadwyservice.customer.domain.events

import yadwy.app.yadwyservice.customer.domain.models.CustomerId
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface CustomerEvent : DomainEvent

data class CustomerCreatedEvent(
    val customerId: CustomerId,
    val accountId: Long
) : CustomerEvent
