package yadwy.app.yadwyservice.sharedkernel.infrastructure.publishers

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.sharedkernel.domain.contracts.IntegrationEventPublisher
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

@Component
class SpringIntegrationEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : IntegrationEventPublisher {

    override fun publish(event: IntegrationEvent) {
        applicationEventPublisher.publishEvent(event)
    }

    override fun publishAll(events: Collection<IntegrationEvent>) {
        events.forEach { publish(it) }
    }
}
