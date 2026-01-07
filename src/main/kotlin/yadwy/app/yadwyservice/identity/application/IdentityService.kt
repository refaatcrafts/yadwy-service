package yadwy.app.yadwyservice.identity.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.models.Account

@Service
class IdentityService(
    private val repository: AccountRepository,
    private val encryptionService: EncryptionService,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun register(phone: String, password: String) {
        if (repository.findByPhoneNumber(phone) != null) {
            throw IllegalArgumentException("Phone number already exists")
        }

        // Use Domain Logic to create account
        val newAccount = Account.register(phone, password, encryptionService)
        repository.save(newAccount)
    }

    fun login(phone: String, password: String): String {
        val account = repository.findByPhoneNumber(phone)
            ?: throw IllegalArgumentException("User not found")

        // Use Domain Logic to perform login
        return account.login(password, encryptionService, tokenProvider)
    }
}