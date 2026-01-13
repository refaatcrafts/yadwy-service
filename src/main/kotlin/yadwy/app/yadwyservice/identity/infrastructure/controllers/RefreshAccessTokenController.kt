package yadwy.app.yadwyservice.identity.infrastructure.controllers

import app.yadwy.api.TokenApi
import app.yadwy.model.RefreshTokenRequestDto
import app.yadwy.model.RefreshTokenResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.RefreshTokenRequest
import yadwy.app.yadwyservice.identity.application.usecases.RefreshAccessToken

@RestController
class RefreshAccessTokenController(
    private val refreshAccessToken: RefreshAccessToken
) : TokenApi {

    override fun refreshAccessToken(refreshTokenRequestDto: RefreshTokenRequestDto):
        ResponseEntity<RefreshTokenResponseDto> {

        val request = RefreshTokenRequest(
            refreshToken = refreshTokenRequestDto.refreshToken
        )

        val response = refreshAccessToken.execute(request)

        return ResponseEntity.ok(
            RefreshTokenResponseDto(
                accessToken = response.accessToken
            )
        )
    }
}
