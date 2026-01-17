package yadwy.app.yadwyservice.identity.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

sealed interface IdentityIntegrationEvent : IntegrationEvent

data class SellerAccountCreatedIntegrationEvent(
    val accountId: Long,
    val name: String
) : IdentityIntegrationEvent

data class CustomerAccountCreatedIntegrationEvent(
    val accountId: Long,
    val name: String
) : IdentityIntegrationEvent
