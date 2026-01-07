package yadwy.app.yadwyservice.identity.api

import app.yadwy.api.AuthenticationApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.LoginRequest
import yadwy.app.yadwyservice.identity.application.usecases.Login

@RestController
class LoginController(
    private val login: Login
) : AuthenticationApi {

    override fun login(loginRequestDto: app.yadwy.model.LoginRequestDto):
        ResponseEntity<app.yadwy.model.LoginResponseDto> {

        val request = LoginRequest(
            phoneNumber = loginRequestDto.phoneNumber,
            password = loginRequestDto.password
        )

        val response = login.execute(request)

        return ResponseEntity.ok(
            app.yadwy.model.LoginResponseDto(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
