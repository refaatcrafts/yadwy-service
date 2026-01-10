package yadwy.app.yadwyservice.customer.domain.models

import yadwy.app.yadwyservice.customer.domain.events.CustomerCreatedEvent
import yadwy.app.yadwyservice.customer.domain.events.CustomerEvent
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot

class Customer internal constructor(
    private val customerId: CustomerId,
    private val accountId: Long,
    private val customerName: CustomerName
) : AggregateRoot<CustomerId, CustomerEvent>(id = customerId) {

    companion object {
        fun create(
            accountId: Long,
            customerName: String
        ): Customer {
            val customer = Customer(
                customerId = CustomerId(0),
                accountId = accountId,
                customerName = CustomerName(customerName)
            )
            customer.raiseEvent(CustomerCreatedEvent(customer.customerId, accountId))
            return customer
        }
    }

    fun getId() = customerId
    fun getAccountId() = accountId
    fun getCustomerName() = customerName
}
