package yadwy.app.yadwyservice.identity.domain.events

import yadwy.app.yadwyservice.identity.domain.models.AccountId
import yadwy.app.yadwyservice.identity.domain.models.Role
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface IdentityEvent : DomainEvent

data class AccountCreatedEvent(val accountId: AccountId, val roles: Set<Role>) : IdentityEvent
