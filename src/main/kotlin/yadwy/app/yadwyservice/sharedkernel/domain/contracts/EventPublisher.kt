package yadwy.app.yadwyservice.sharedkernel.domain.contracts

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

interface EventPublisher {
    fun publish(event: DomainEvent)
    fun publishAll(events: Collection<DomainEvent>)
}
