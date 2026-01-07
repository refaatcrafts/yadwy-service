package yadwy.app.yadwyservice.identity.infrastructure.service

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.TokenProvider
import yadwy.app.yadwyservice.identity.domain.models.Account
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtTokenProvider(private val jwtEncoder: JwtEncoder) : TokenProvider {

    override fun createAccessToken(account: Account): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("yadwy-service")
            .issuedAt(now)
            .expiresAt(now.plus(24, ChronoUnit.HOURS))
            .subject(account.getPhoneNumber().value)
            .claim("id", account.getId().id)
            .build()

        val params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)
        return jwtEncoder.encode(params).tokenValue
    }

    override fun createRefreshToken(account: Account): String {
        TODO("Not yet implemented")
    }
}