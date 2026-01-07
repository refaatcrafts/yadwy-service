package yadwy.app.yadwyservice.identity.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.models.Account
import yadwy.app.yadwyservice.identity.domain.models.AccountId
import yadwy.app.yadwyservice.identity.domain.models.Name
import yadwy.app.yadwyservice.identity.domain.models.PhoneNumber
import yadwy.app.yadwyservice.identity.infrastructure.database.dao.AccountDao
import yadwy.app.yadwyservice.identity.infrastructure.database.dbo.AccountDbo

@Component
class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val encryptionService: EncryptionService
) : AccountRepository {
    override fun save(account: Account): Account {
        val savedAccount = accountDao.save(
            AccountDbo(
                name = account.getName().value,
                phoneNumber = account.getPhoneNumber().value,
                passwordHash = account.getPasswordHash()
            )
        )

        return Account(
            accountId = AccountId(savedAccount.id!!),
            name = Name(savedAccount.name),
            phoneNumber = PhoneNumber(savedAccount.phoneNumber),
            passwordHash = encryptionService.encrypt(savedAccount.passwordHash)
                ?: throw IllegalStateException("Encryption failed")
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
            )
        }
    }
}