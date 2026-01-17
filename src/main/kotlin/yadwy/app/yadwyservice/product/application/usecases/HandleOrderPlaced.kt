package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.order.api.OrderPlacedIntegrationEvent
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.sharedkernel.application.EventHandler

@Component
class HandleOrderPlaced(
    private val productRepository: ProductRepository
) : EventHandler<OrderPlacedIntegrationEvent, Unit>() {

    override fun handle(event: OrderPlacedIntegrationEvent) {
        event.lineItems.forEach { lineItem ->
            try {
                val product = productRepository.findById(lineItem.productId)
                if (product == null) {
                    logger.warn("Product not found: {}", lineItem.productId)
                    return@forEach
                }

                product.decrementStock(lineItem.quantity)
                productRepository.save(product)
                logger.debug("Deducted {} units from product {}", lineItem.quantity, lineItem.productId)
            } catch (e: Exception) {
                logger.error(
                    "Failed to deduct inventory for productId: {}, quantity: {}. Error: {}",
                    lineItem.productId, lineItem.quantity, e.message
                )
            }
        }
    }
}
