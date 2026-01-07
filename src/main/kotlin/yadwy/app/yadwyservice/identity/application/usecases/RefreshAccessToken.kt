package yadwy.app.yadwyservice.identity.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.identity.application.models.RefreshTokenRequest
import yadwy.app.yadwyservice.identity.application.models.RefreshTokenResponse
import yadwy.app.yadwyservice.identity.domain.contracts.AccountRepository
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidTokenException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class RefreshAccessToken(
    private val accountRepository: AccountRepository,
    private val tokenProvider: TokenProvider
) : UseCase<RefreshTokenRequest, RefreshTokenResponse>() {

    override fun execute(request: RefreshTokenRequest): RefreshTokenResponse {
        logger.info("Refresh token request received")

        val accountId = try {
            tokenProvider.validateRefreshToken(request.refreshToken)
        } catch (e: Exception) {
            throw InvalidTokenException("Invalid refresh token: ${e.message}")
        }

        val account = accountRepository.findById(accountId)
            ?: throw InvalidTokenException("Account not found")

        val newAccessToken = tokenProvider.createAccessToken(account)

        logger.info("Access token refreshed for account: ${account.getId().id}")

        return RefreshTokenResponse(accessToken = newAccessToken)
    }
}