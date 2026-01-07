package yadwy.app.yadwyservice.identity.application.usecases

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yadwy.app.yadwyservice.identity.application.models.RegisterSellerRequest
import yadwy.app.yadwyservice.identity.application.models.RegisterSellerResponse
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.exceptions.AccountAlreadyExistsException
import yadwy.app.yadwyservice.identity.domain.models.Account
import yadwy.app.yadwyservice.identity.domain.models.Role
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class RegisterSeller(
    private val accountRepository: AccountRepository,
    private val encryptionService: EncryptionService,
    private val tokenProvider: TokenProvider
) : UseCase<RegisterSellerRequest, RegisterSellerResponse>() {

    @Transactional
    override fun execute(request: RegisterSellerRequest): RegisterSellerResponse {
        logger.info("Registering new seller with phone: ${request.phoneNumber}")

        if (accountRepository.findByPhoneNumber(request.phoneNumber) != null) {
            throw AccountAlreadyExistsException(request.phoneNumber)
        }

        val account = Account.register(
            name = request.name,
            phone = request.phoneNumber,
            rawPassword = request.password,
            roles = mutableSetOf(Role.SELLER),
            encryptionService = encryptionService
        )

        val savedAccount = accountRepository.save(account)

        val accessToken = tokenProvider.createAccessToken(savedAccount)
        val refreshToken = tokenProvider.createRefreshToken(savedAccount)

        logger.info("Seller registered successfully with ID: ${savedAccount.getId().id}")

        return RegisterSellerResponse(
            accountId = savedAccount.getId().id,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}