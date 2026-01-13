package yadwy.app.yadwyservice.identity.infrastructure.controllers

import app.yadwy.api.LoginApi
import app.yadwy.model.LoginRequestDto
import app.yadwy.model.LoginResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.LoginRequest
import yadwy.app.yadwyservice.identity.application.usecases.Login

@RestController
class LoginController(
    private val login: Login
) : LoginApi {

    override fun login(loginRequestDto: LoginRequestDto):
        ResponseEntity<LoginResponseDto> {

        val request = LoginRequest(
            phoneNumber = loginRequestDto.phoneNumber,
            password = loginRequestDto.password
        )

        val response = login.execute(request)

        return ResponseEntity.ok(
            LoginResponseDto(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
