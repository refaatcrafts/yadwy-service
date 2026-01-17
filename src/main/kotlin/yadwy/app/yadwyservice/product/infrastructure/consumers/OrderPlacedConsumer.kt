package yadwy.app.yadwyservice.product.infrastructure.consumers

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import yadwy.app.yadwyservice.order.api.OrderPlacedIntegrationEvent
import yadwy.app.yadwyservice.product.application.usecases.HandleOrderPlaced

@Component
class OrderPlacedConsumer(
    private val handleOrderPlaced: HandleOrderPlaced
) {
    private val logger = LoggerFactory.getLogger(OrderPlacedConsumer::class.java)

    @TransactionalEventListener
    fun onOrderPlaced(event: OrderPlacedIntegrationEvent) {
        logger.info("Processing OrderPlacedIntegrationEvent for orderId: {}", event.orderId)
        handleOrderPlaced.handle(event)
    }
}