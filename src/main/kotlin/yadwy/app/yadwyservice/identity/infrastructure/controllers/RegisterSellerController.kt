package yadwy.app.yadwyservice.identity.infrastructure.controllers

import app.yadwy.api.SellerRegistrationApi
import app.yadwy.model.RegisterSellerRequestDto
import app.yadwy.model.RegisterSellerResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.RegisterSellerRequest
import yadwy.app.yadwyservice.identity.application.usecases.RegisterSeller

@RestController
class RegisterSellerController(
    private val registerSeller: RegisterSeller
) : SellerRegistrationApi {

    override fun registerSeller(registerSellerRequestDto: RegisterSellerRequestDto):
        ResponseEntity<RegisterSellerResponseDto> {

        val request = RegisterSellerRequest(
            name = registerSellerRequestDto.name,
            phoneNumber = registerSellerRequestDto.phoneNumber,
            password = registerSellerRequestDto.password
        )

        val response = registerSeller.execute(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            RegisterSellerResponseDto(
                accountId = response.accountId,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
