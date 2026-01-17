package yadwy.app.yadwyservice.customer.infrastructure.consumers

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import yadwy.app.yadwyservice.customer.application.usecases.HandleCustomerAccountCreated
import yadwy.app.yadwyservice.identity.api.CustomerAccountCreatedIntegrationEvent

@Component
class CustomerAccountCreatedConsumer(
    private val handleCustomerAccountCreated: HandleCustomerAccountCreated
) {
    private val logger = LoggerFactory.getLogger(CustomerAccountCreatedConsumer::class.java)

    @Async
    @TransactionalEventListener
    fun onCustomerAccountCreated(event: CustomerAccountCreatedIntegrationEvent) {
        logger.info("Processing CustomerAccountCreated for accountId: {}", event.accountId)

        try {
            handleCustomerAccountCreated.handle(event.accountId, event.name)
        } catch (e: Exception) {
            logger.error("Failed to process customer account created event", e)
        }
    }
}
