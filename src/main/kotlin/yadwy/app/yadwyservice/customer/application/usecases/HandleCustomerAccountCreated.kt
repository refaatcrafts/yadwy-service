package yadwy.app.yadwyservice.customer.application.usecases

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.customer.domain.contracts.CustomerRepository
import yadwy.app.yadwyservice.customer.domain.models.Customer

@Component
class HandleCustomerAccountCreated(
    private val customerRepository: CustomerRepository
) {
    private val logger = LoggerFactory.getLogger(HandleCustomerAccountCreated::class.java)

    fun handle(accountId: Long, name: String) {
        val existingCustomer = customerRepository.findByAccountId(accountId)
        if (existingCustomer != null) {
            logger.warn("Customer already exists for accountId: {}", accountId)
            return
        }

        val customer = Customer.create(
            accountId = accountId,
            customerName = name
        )

        customerRepository.save(customer)
        logger.info("Created customer for accountId: {}", accountId)
    }
}
