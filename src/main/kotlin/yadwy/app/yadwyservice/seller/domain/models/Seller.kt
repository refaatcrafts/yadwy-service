package yadwy.app.yadwyservice.seller.domain.models

import yadwy.app.yadwyservice.seller.domain.events.SellerCreatedEvent
import yadwy.app.yadwyservice.seller.domain.events.SellerEvent
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot

class Seller internal constructor(
    private val sellerId: SellerId,
    private val accountId: Long,
    private val storeName: StoreName
) : AggregateRoot<SellerId, SellerEvent>(id = sellerId) {

    companion object {
        fun create(
            accountId: Long,
            storeName: String
        ): Seller {
            val seller = Seller(
                sellerId = SellerId(0),
                accountId = accountId,
                storeName = StoreName(storeName)
            )
            seller.raiseEvent(SellerCreatedEvent(seller.sellerId, accountId))
            return seller
        }
    }

    fun getId() = sellerId
    fun getAccountId() = accountId
    fun getStoreName() = storeName
}
