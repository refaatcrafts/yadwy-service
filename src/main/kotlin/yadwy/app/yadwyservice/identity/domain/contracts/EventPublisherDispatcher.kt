package yadwy.app.yadwyservice.identity.domain.contracts

import yadwy.app.yadwyservice.identity.domain.events.IdentityEvent

interface EventPublisherDispatcher {
    fun dispatch(event: IdentityEvent)
    fun dispatchAll(events: Collection<IdentityEvent>)
}
