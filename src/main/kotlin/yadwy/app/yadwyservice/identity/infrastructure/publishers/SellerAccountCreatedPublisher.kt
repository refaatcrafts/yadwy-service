package yadwy.app.yadwyservice.identity.infrastructure.publishers

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import yadwy.app.yadwyservice.identity.domain.events.AccountCreatedEvent

@Component
class SellerAccountCreatedPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(SellerAccountCreatedPublisher::class.java)

    fun publish(event: AccountCreatedEvent) {
        logger.info("Publishing SellerAccountCreated for accountId: {}", event.accountId.id)

        val dto = SellerAccountCreatedDto(
            accountId = event.accountId.id,
            name = event.name,
            roles = event.roles.map { it.name }.toSet()
        )

        val jsonString = objectMapper.writeValueAsString(dto)
        applicationEventPublisher.publishEvent(jsonString)

        logger.debug("Published JSON: {}", jsonString)
    }


    data class SellerAccountCreatedDto(
        val accountId: Long,
        val name: String,
        val roles: Set<String>
    )
}
