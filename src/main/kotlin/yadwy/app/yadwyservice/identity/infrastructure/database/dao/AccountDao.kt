package yadwy.app.yadwyservice.identity.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.identity.infrastructure.database.dbo.AccountDbo

interface AccountDao : ListCrudRepository<AccountDbo, Long> {

    fun findByPhoneNumber(phone: String): AccountDbo?
}