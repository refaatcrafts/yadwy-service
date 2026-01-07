package yadwy.app.yadwyservice.identity.domain.events

import yadwy.app.yadwyservice.identity.domain.models.AccountId
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface IdentityEvent : DomainEvent

open class AccountCreatedEvent(val accountId: AccountId) : IdentityEvent
