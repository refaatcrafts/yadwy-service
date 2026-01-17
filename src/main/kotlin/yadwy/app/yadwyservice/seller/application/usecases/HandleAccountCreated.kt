package yadwy.app.yadwyservice.seller.application.usecases

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.seller.domain.contracts.SellerRepository
import yadwy.app.yadwyservice.seller.domain.models.Seller

@Component
class HandleAccountCreated(
    private val sellerRepository: SellerRepository
) {
    private val logger = LoggerFactory.getLogger(HandleAccountCreated::class.java)

    fun handle(accountId: Long, name: String) {
        val existingSeller = sellerRepository.findByAccountId(accountId)
        if (existingSeller != null) {
            logger.warn("Seller already exists for accountId: {}", accountId)
            return
        }

        val seller = Seller.create(
            accountId = accountId,
            storeName = "Store-$accountId"
        )

        sellerRepository.save(seller)
        logger.info("Created seller for accountId: {}", accountId)
    }
}
