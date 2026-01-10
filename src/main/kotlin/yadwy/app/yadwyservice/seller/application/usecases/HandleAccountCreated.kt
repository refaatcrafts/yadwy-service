package yadwy.app.yadwyservice.seller.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.seller.domain.contracts.SellerRepository
import yadwy.app.yadwyservice.seller.domain.models.Seller
import yadwy.app.yadwyservice.seller.infrastructure.consumers.SellerAccountCreatedDto
import yadwy.app.yadwyservice.sharedkernel.application.EventHandler

@Component
class HandleAccountCreated(
    private val sellerRepository: SellerRepository
) : EventHandler<SellerAccountCreatedDto, Unit>() {

    override fun handle(event: SellerAccountCreatedDto) {
        val existingSeller = sellerRepository.findByAccountId(event.accountId)
        if (existingSeller != null) {
            logger.warn("Seller already exists for accountId: {}", event.accountId)
            return
        }

        val seller = Seller.create(
            accountId = event.accountId,
            storeName = "Store-${event.accountId}"
        )

        sellerRepository.save(seller)
        logger.info("Created seller for accountId: {}", event.accountId)
    }
}
