package yadwy.app.yadwyservice.customer.domain.contracts

import yadwy.app.yadwyservice.customer.domain.models.Customer

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(customerId: Long): Customer?
    fun findByAccountId(accountId: Long): Customer?
}
