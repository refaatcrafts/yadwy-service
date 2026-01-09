package yadwy.app.yadwyservice.sharedkernel.infrastructure.publishers

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.sharedkernel.domain.contracts.EventPublisher
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

@Component
class SpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : EventPublisher {

    private val logger = LoggerFactory.getLogger(SpringEventPublisher::class.java)

    override fun publish(event: DomainEvent) {
        logger.info("Publishing domain event: {}", event::class.simpleName)
        applicationEventPublisher.publishEvent(event)
    }

    override fun publishAll(events: Collection<DomainEvent>) {
        if (events.isEmpty()) return
        logger.info("Publishing {} domain events", events.size)
        events.forEach { publish(it) }
    }
}
