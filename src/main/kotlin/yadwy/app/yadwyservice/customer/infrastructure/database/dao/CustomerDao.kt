package yadwy.app.yadwyservice.customer.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.customer.infrastructure.database.dbo.CustomerDbo

interface CustomerDao : ListCrudRepository<CustomerDbo, Long> {
    fun findByAccountId(accountId: Long): CustomerDbo?
}
