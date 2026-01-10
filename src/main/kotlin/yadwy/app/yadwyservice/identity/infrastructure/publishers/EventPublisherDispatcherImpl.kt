package yadwy.app.yadwyservice.identity.infrastructure.publishers

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.EventPublisherDispatcher
import yadwy.app.yadwyservice.identity.domain.events.AccountCreatedEvent
import yadwy.app.yadwyservice.identity.domain.events.IdentityEvent
import yadwy.app.yadwyservice.identity.domain.models.Role

@Component
class EventPublisherDispatcherImpl(
    private val sellerAccountCreatedPublisher: SellerAccountCreatedPublisher,
    private val customerAccountCreatedPublisher: CustomerAccountCreatedPublisher
) : EventPublisherDispatcher {
    private val logger = LoggerFactory.getLogger(EventPublisherDispatcherImpl::class.java)

    override fun dispatch(event: IdentityEvent) {
        when (event) {
            is AccountCreatedEvent -> dispatchAccountCreated(event)
            // Add more event types here as needed
        }
    }

    override fun dispatchAll(events: Collection<IdentityEvent>) {
        events.forEach { dispatch(it) }
    }

    private fun dispatchAccountCreated(event: AccountCreatedEvent) {
        when {
            event.roles.contains(Role.SELLER) -> {
                logger.debug("Routing to SellerAccountCreatedPublisher")
                sellerAccountCreatedPublisher.publish(event)
            }

            event.roles.contains(Role.CUSTOMER) -> {
                logger.debug("Routing to CustomerAccountCreatedPublisher")
                customerAccountCreatedPublisher.publish(event)
            }

            else -> {
                logger.warn("No publisher found for AccountCreatedEvent with roles: {}", event.roles)
            }
        }
    }
}
