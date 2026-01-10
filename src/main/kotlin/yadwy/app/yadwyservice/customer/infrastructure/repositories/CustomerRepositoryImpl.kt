package yadwy.app.yadwyservice.customer.infrastructure.repositories

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.customer.domain.contracts.CustomerRepository
import yadwy.app.yadwyservice.customer.domain.models.Customer
import yadwy.app.yadwyservice.customer.domain.models.CustomerId
import yadwy.app.yadwyservice.customer.domain.models.CustomerName
import yadwy.app.yadwyservice.customer.infrastructure.database.dao.CustomerDao
import yadwy.app.yadwyservice.customer.infrastructure.database.dbo.CustomerDbo

@Component
class CustomerRepositoryImpl(
    private val customerDao: CustomerDao,
    private val eventPublisher: ApplicationEventPublisher
) : CustomerRepository {

    override fun save(customer: Customer): Customer {
        val savedDbo = customerDao.save(
            CustomerDbo(
                accountId = customer.getAccountId(),
                customerName = customer.getCustomerName().value
            )
        )

        val events = customer.occurredEvents()
        events.forEach { eventPublisher.publishEvent(it) }

        return Customer(
            customerId = CustomerId(savedDbo.id!!),
            accountId = savedDbo.accountId,
            customerName = CustomerName(savedDbo.customerName)
        )
    }

    override fun findById(customerId: Long): Customer? {
        return customerDao.findById(customerId).orElse(null)?.toDomain()
    }

    override fun findByAccountId(accountId: Long): Customer? {
        return customerDao.findByAccountId(accountId)?.toDomain()
    }

    private fun CustomerDbo.toDomain(): Customer = Customer(
        customerId = CustomerId(id!!),
        accountId = accountId,
        customerName = CustomerName(customerName)
    )
}
