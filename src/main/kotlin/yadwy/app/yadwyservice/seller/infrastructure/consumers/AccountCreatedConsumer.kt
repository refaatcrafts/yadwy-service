package yadwy.app.yadwyservice.seller.infrastructure.consumers

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import yadwy.app.yadwyservice.identity.api.SellerAccountCreatedIntegrationEvent
import yadwy.app.yadwyservice.seller.application.usecases.HandleAccountCreated

@Component
class AccountCreatedConsumer(
    private val handleAccountCreated: HandleAccountCreated
) {
    private val logger = LoggerFactory.getLogger(AccountCreatedConsumer::class.java)

    @Async
    @TransactionalEventListener
    fun onSellerAccountCreated(event: SellerAccountCreatedIntegrationEvent) {
        logger.info("Processing SellerAccountCreated for accountId: {}", event.accountId)

        try {
            handleAccountCreated.handle(event.accountId, event.name)
        } catch (e: Exception) {
            logger.error("Failed to process seller account created event", e)
        }
    }
}