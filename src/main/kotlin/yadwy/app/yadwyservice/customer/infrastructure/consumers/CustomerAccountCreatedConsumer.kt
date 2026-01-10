package yadwy.app.yadwyservice.customer.infrastructure.consumers

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import yadwy.app.yadwyservice.customer.application.usecases.HandleCustomerAccountCreated

@Component
class CustomerAccountCreatedConsumer(
    private val handleCustomerAccountCreated: HandleCustomerAccountCreated,
    private val mapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(CustomerAccountCreatedConsumer::class.java)

    @EventListener
    fun onCustomerAccountCreated(jsonString: String) {
        logger.debug("Received JSON event: {}", jsonString)

        try {
            val dto = mapper.readValue(jsonString, CustomerAccountCreatedDto::class.java)

            logger.info("Processing CustomerAccountCreated for accountId: {}", dto.accountId)

            handleCustomerAccountCreated.handle(dto)

        } catch (e: Exception) {
            logger.error("Failed to process customer account created event", e)
        }
    }
}

data class CustomerAccountCreatedDto(
    val accountId: Long,
    val name: String,
    val roles: Set<String>
)
