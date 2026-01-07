package yadwy.app.yadwyservice.identity.api

import app.yadwy.api.AuthenticationApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.RefreshTokenRequest
import yadwy.app.yadwyservice.identity.application.usecases.RefreshAccessToken

@RestController
class RefreshAccessTokenController(
    private val refreshAccessToken: RefreshAccessToken
) : AuthenticationApi {

    override fun refreshAccessToken(refreshTokenRequestDto: app.yadwy.model.RefreshTokenRequestDto):
        ResponseEntity<app.yadwy.model.RefreshTokenResponseDto> {

        val request = RefreshTokenRequest(
            refreshToken = refreshTokenRequestDto.refreshToken
        )

        val response = refreshAccessToken.execute(request)

        return ResponseEntity.ok(
            app.yadwy.model.RefreshTokenResponseDto(
                accessToken = response.accessToken
            )
        )
    }
}
