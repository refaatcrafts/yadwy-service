package yadwy.app.yadwyservice.seller.infrastructure.consumers

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import yadwy.app.yadwyservice.seller.application.usecases.HandleAccountCreated

@Component
class AccountCreatedConsumer(
    private val handleAccountCreated: HandleAccountCreated,
    private val mapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(AccountCreatedConsumer::class.java)

    @EventListener
    fun onSellerAccountCreated(jsonString: String) {
        logger.debug("Received JSON event: {}", jsonString)

        try {
            val dto = mapper.readValue(jsonString, SellerAccountCreatedDto::class.java)

            logger.info("Processing SellerAccountCreated for accountId: {}", dto.accountId)

            handleAccountCreated.handle(dto)

        } catch (e: Exception) {
            logger.error("Failed to process seller account created event", e)
        }
    }
}

data class SellerAccountCreatedDto(
    val accountId: Long,
    val name: String,
    val roles: Set<String>
)