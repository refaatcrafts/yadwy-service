package yadwy.app.yadwyservice.seller.domain.events

import yadwy.app.yadwyservice.seller.domain.models.SellerId
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface SellerEvent : DomainEvent

data class SellerCreatedEvent(
    val sellerId: SellerId,
    val accountId: Long
) : SellerEvent
