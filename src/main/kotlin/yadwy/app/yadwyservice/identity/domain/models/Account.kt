package yadwy.app.yadwyservice.identity.domain.models

import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider

@JvmInline
value class AccountId(val id: Long)

class Account(
    private val id: AccountId,
    private val name: Name,
    private val phoneNumber: PhoneNumber,
    private val passwordHash: String,
//    private val roles: MutableSet<Role> = mutableSetOf(),

) {
    companion object {
        fun register(phone: String, rawPassword: String, encryptionService: EncryptionService): Account {
            // Domain Validation
            if (phone.isBlank()) throw IllegalArgumentException("Phone cannot be empty")
            if (rawPassword.length < 6) throw IllegalArgumentException("Password too short")

            return Account(
                id = AccountId(0),
                name = Name("test"),
                phoneNumber = PhoneNumber(phone),
                passwordHash = encryptionService.encrypt(rawPassword)
                    ?: throw IllegalStateException("Encryption failed")
            )
        }
    }

    fun login(rawPassword: String, encryptionService: EncryptionService, tokenProvider: TokenProvider): String {
        if (!encryptionService.matches(rawPassword, this.passwordHash)) {
            throw IllegalArgumentException("Invalid credentials")
        }

        // If successful, generate token
        return tokenProvider.createAccessToken(this)
    }


    fun getId() = id
    fun getName() = name
    fun getPhoneNumber() = phoneNumber
    fun getPasswordHash() = passwordHash
}