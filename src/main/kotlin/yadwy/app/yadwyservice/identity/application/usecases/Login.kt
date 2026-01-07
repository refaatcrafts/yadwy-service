package yadwy.app.yadwyservice.identity.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.identity.application.models.LoginRequest
import yadwy.app.yadwyservice.identity.application.models.LoginResponse
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidCredentialsException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class Login(
    private val accountRepository: AccountRepository,
    private val encryptionService: EncryptionService,
    private val tokenProvider: TokenProvider
) : UseCase<LoginRequest, LoginResponse>() {

    override fun execute(request: LoginRequest): LoginResponse {
        logger.info("Login attempt for phone: ${request.phoneNumber}")

        val account = accountRepository.findByPhoneNumber(request.phoneNumber)
            ?: throw InvalidCredentialsException()

        val (accessToken, refreshToken) = try {
            account.login(request.password, encryptionService, tokenProvider)
        } catch (e: IllegalArgumentException) {
            throw InvalidCredentialsException()
        }

        logger.info("User logged in successfully: ${account.getId().id}")

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}