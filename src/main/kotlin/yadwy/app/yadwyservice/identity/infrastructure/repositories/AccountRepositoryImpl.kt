package yadwy.app.yadwyservice.identity.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EventPublisherDispatcher
import yadwy.app.yadwyservice.identity.domain.events.AccountCreatedEvent
import yadwy.app.yadwyservice.identity.domain.models.Account
import yadwy.app.yadwyservice.identity.domain.models.AccountId
import yadwy.app.yadwyservice.identity.domain.models.Name
import yadwy.app.yadwyservice.identity.domain.models.PhoneNumber
import yadwy.app.yadwyservice.identity.infrastructure.database.dao.AccountDao
import yadwy.app.yadwyservice.identity.infrastructure.database.dbo.AccountDbo

@Component
class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val eventPublisherDispatcher: EventPublisherDispatcher
) : AccountRepository {
    override fun save(account: Account): Account {
        val savedAccount = accountDao.save(
            AccountDbo(
                name = account.getName().value,
                phoneNumber = account.getPhoneNumber().value,
                passwordHash = account.getPasswordHash(),
                roles = account.getRoles().toList()
            )
        )

        val persistedAccountId = AccountId(savedAccount.id!!)

        // Map events to use the persisted ID instead of the placeholder (0)
        val eventsWithCorrectId = account.occurredEvents().map { event ->
            when (event) {
                is AccountCreatedEvent -> event.copy(accountId = persistedAccountId)
            }
        }
        eventPublisherDispatcher.dispatchAll(eventsWithCorrectId)

        return Account(
            accountId = persistedAccountId,
            name = Name(savedAccount.name),
            phoneNumber = PhoneNumber(savedAccount.phoneNumber),
            passwordHash = savedAccount.passwordHash,
            roles = savedAccount.roles.toMutableSet()
        )
    }

    override fun findByPhoneNumber(phone: String): Account? {
        val accountDbo = accountDao.findByPhoneNumber(phone)
        return accountDbo?.let {
            Account(
                accountId = AccountId(accountDbo.id!!),
                name = Name(accountDbo.name),
                phoneNumber = PhoneNumber(accountDbo.phoneNumber),
                passwordHash = accountDbo.passwordHash,
                roles = accountDbo.roles.toMutableSet()
            )
        }
    }

    override fun findById(accountId: Long): Account? {
        val accountDbo = accountDao.findById(accountId).orElse(null)
        return accountDbo?.let {
            Account(
                accountId = AccountId(accountDbo.id!!),
                name = Name(accountDbo.name),
                phoneNumber = PhoneNumber(accountDbo.phoneNumber),
                passwordHash = accountDbo.passwordHash,
                roles = accountDbo.roles.toMutableSet()
            )
        }
    }
}