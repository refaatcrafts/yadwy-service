package yadwy.app.yadwyservice.sharedkernel.domain.contracts

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

interface IntegrationEventPublisher {

    fun publish(event: IntegrationEvent)
    
    fun publishAll(events: Collection<IntegrationEvent>)
}
