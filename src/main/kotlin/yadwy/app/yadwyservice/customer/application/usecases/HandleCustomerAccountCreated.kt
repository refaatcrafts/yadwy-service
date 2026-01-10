package yadwy.app.yadwyservice.customer.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.customer.domain.contracts.CustomerRepository
import yadwy.app.yadwyservice.customer.domain.models.Customer
import yadwy.app.yadwyservice.customer.infrastructure.consumers.CustomerAccountCreatedDto
import yadwy.app.yadwyservice.sharedkernel.application.EventHandler

@Component
class HandleCustomerAccountCreated(
    private val customerRepository: CustomerRepository
) : EventHandler<CustomerAccountCreatedDto, Unit>() {

    override fun handle(event: CustomerAccountCreatedDto) {
        val existingCustomer = customerRepository.findByAccountId(event.accountId)
        if (existingCustomer != null) {
            logger.warn("Customer already exists for accountId: {}", event.accountId)
            return
        }

        val customer = Customer.create(
            accountId = event.accountId,
            customerName = event.name
        )

        customerRepository.save(customer)
        logger.info("Created customer for accountId: {}", event.accountId)
    }
}
