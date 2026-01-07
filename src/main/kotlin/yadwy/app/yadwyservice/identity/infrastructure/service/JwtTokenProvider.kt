package yadwy.app.yadwyservice.identity.infrastructure.service

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidTokenException
import yadwy.app.yadwyservice.identity.domain.models.Account
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtTokenProvider(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder
) : TokenProvider {

    override fun createAccessToken(account: Account): String {
        val now = Instant.now()
        val rolesList = account.getRoles().map { it.name }
        val claims = JwtClaimsSet.builder()
            .issuer("yadwy-service")
            .issuedAt(now)
            .expiresAt(now.plus(24, ChronoUnit.HOURS))
            .subject(account.getPhoneNumber().value)
            .claim("id", account.getId().id)
            .claim("roles", rolesList)
            .claim("type", "access")
            .build()

        val params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)
        return jwtEncoder.encode(params).tokenValue
    }

    override fun createRefreshToken(account: Account): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("yadwy-service")
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.DAYS))
            .subject(account.getPhoneNumber().value)
            .claim("id", account.getId().id)
            .claim("type", "refresh")
            .build()

        val params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)
        return jwtEncoder.encode(params).tokenValue
    }

    override fun validateRefreshToken(token: String): Long {
        try {
            val jwt = jwtDecoder.decode(token)

            val tokenType = jwt.getClaim<String>("type")
            if (tokenType != "refresh") {
                throw InvalidTokenException("Token is not a refresh token")
            }

            val accountId = jwt.getClaim<Long>("id")
                ?: throw InvalidTokenException("Missing account ID claim")

            return accountId
        } catch (e: JwtException) {
            throw InvalidTokenException("Token validation failed: ${e.message}")
        }
    }
}