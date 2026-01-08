package yadwy.app.yadwyservice.identity.api

import app.yadwy.api.SellerRegistrationApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.RegisterSellerRequest
import yadwy.app.yadwyservice.identity.application.usecases.RegisterSeller

@RestController
class RegisterSellerController(
    private val registerSeller: RegisterSeller
) : SellerRegistrationApi {

    override fun registerSeller(registerSellerRequestDto: app.yadwy.model.RegisterSellerRequestDto):
        ResponseEntity<app.yadwy.model.RegisterSellerResponseDto> {

        val request = RegisterSellerRequest(
            name = registerSellerRequestDto.name,
            phoneNumber = registerSellerRequestDto.phoneNumber,
            password = registerSellerRequestDto.password
        )

        val response = registerSeller.execute(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            app.yadwy.model.RegisterSellerResponseDto(
                accountId = response.accountId,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
