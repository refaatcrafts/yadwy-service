package yadwy.app.yadwyservice.identity.domain.models

import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.events.AccountCreatedEvent
import yadwy.app.yadwyservice.identity.domain.events.IdentityEvent
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class AccountId(val id: Long) : ValueObject

class Account(
    private val accountId: AccountId,
    private val name: Name,
    private val phoneNumber: PhoneNumber,
    private val passwordHash: String,
    private val roles: MutableSet<Role>
) : AggregateRoot<AccountId, IdentityEvent>(id = accountId) {
    companion object {
        fun register(
            name: String,
            phone: String,
            rawPassword: String,
            roles: MutableSet<Role>,
            encryptionService: EncryptionService
        ): Account {
            if (phone.isBlank()) throw yadwy.app.yadwyservice.identity.domain.exceptions.InvalidPhoneNumberException(phone)
            if (rawPassword.length < 6) throw yadwy.app.yadwyservice.identity.domain.exceptions.InvalidPasswordException("Password must be at least 6 characters")

            val encryptedPassword = encryptionService.encrypt(rawPassword)
                ?: throw yadwy.app.yadwyservice.identity.domain.exceptions.EncryptionFailedException()

            val account = Account(
                accountId = AccountId(0),
                name = Name(name),
                phoneNumber = PhoneNumber(phone),
                passwordHash = encryptedPassword,
                roles = roles
            )

            account.raiseEvent(AccountCreatedEvent(account.accountId, roles.toSet()))
            return account
        }
    }

    fun login(rawPassword: String, encryptionService: EncryptionService, tokenProvider: TokenProvider): Pair<String, String> {
        if (!encryptionService.matches(rawPassword, this.passwordHash)) {
            throw yadwy.app.yadwyservice.identity.domain.exceptions.InvalidCredentialsException()
        }

        return Pair(
            tokenProvider.createAccessToken(this),
            tokenProvider.createRefreshToken(this)
        )
    }

    fun getId() = accountId
    fun getName() = name
    fun getPhoneNumber() = phoneNumber
    fun getPasswordHash() = passwordHash
    fun getRoles() = roles.toSet()
}