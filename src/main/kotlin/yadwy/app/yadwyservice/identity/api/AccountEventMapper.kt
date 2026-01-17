package yadwy.app.yadwyservice.identity.api

import yadwy.app.yadwyservice.identity.domain.events.AccountCreatedEvent
import yadwy.app.yadwyservice.identity.domain.events.IdentityEvent
import yadwy.app.yadwyservice.identity.domain.models.AccountId
import yadwy.app.yadwyservice.identity.domain.models.Role
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

fun IdentityEvent.toIntegrationEvent(persistedAccountId: AccountId): IntegrationEvent? {
    return when (this) {
        is AccountCreatedEvent -> this.toIntegrationEvent(persistedAccountId)
    }
}

private fun AccountCreatedEvent.toIntegrationEvent(persistedAccountId: AccountId): IntegrationEvent? {
    return when {
        roles.contains(Role.SELLER) -> SellerAccountCreatedIntegrationEvent(
            accountId = persistedAccountId.id,
            name = name
        )
        roles.contains(Role.CUSTOMER) -> CustomerAccountCreatedIntegrationEvent(
            accountId = persistedAccountId.id,
            name = name
        )
        else -> null
    }
}
