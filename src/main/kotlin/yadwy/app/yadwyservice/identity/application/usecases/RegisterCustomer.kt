package yadwy.app.yadwyservice.identity.application.usecases

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yadwy.app.yadwyservice.identity.application.models.RegisterCustomerRequest
import yadwy.app.yadwyservice.identity.application.models.RegisterCustomerResponse
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.exceptions.AccountAlreadyExistsException
import yadwy.app.yadwyservice.identity.domain.models.Account
import yadwy.app.yadwyservice.identity.domain.models.Role
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class RegisterCustomer(
    private val accountRepository: AccountRepository,
    private val encryptionService: EncryptionService,
    private val tokenProvider: TokenProvider
) : UseCase<RegisterCustomerRequest, RegisterCustomerResponse>() {

    @Transactional
    override fun execute(request: RegisterCustomerRequest): RegisterCustomerResponse {
        logger.info("Registering new customer with phone: ${request.phoneNumber}")

        if (accountRepository.findByPhoneNumber(request.phoneNumber) != null) {
            throw AccountAlreadyExistsException(request.phoneNumber)
        }

        val account = Account.register(
            name = request.name,
            phone = request.phoneNumber,
            rawPassword = request.password,
            roles = mutableSetOf(Role.CUSTOMER),
            encryptionService = encryptionService
        )

        val savedAccount = accountRepository.save(account)

        val accessToken = tokenProvider.createAccessToken(savedAccount)
        val refreshToken = tokenProvider.createRefreshToken(savedAccount)

        logger.info("Customer registered successfully with ID: ${savedAccount.getId().id}")

        return RegisterCustomerResponse(
            accountId = savedAccount.getId().id,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}