package yadwy.app.yadwyservice.identity.api

import app.yadwy.api.CustomerRegistrationApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.identity.application.models.RegisterCustomerRequest
import yadwy.app.yadwyservice.identity.application.usecases.RegisterCustomer

@RestController
class RegisterCustomerController(
    private val registerCustomer: RegisterCustomer
) : CustomerRegistrationApi {

    override fun registerCustomer(registerCustomerRequestDto: app.yadwy.model.RegisterCustomerRequestDto):
        ResponseEntity<app.yadwy.model.RegisterCustomerResponseDto> {

        val request = RegisterCustomerRequest(
            name = registerCustomerRequestDto.name,
            phoneNumber = registerCustomerRequestDto.phoneNumber,
            password = registerCustomerRequestDto.password
        )

        val response = registerCustomer.execute(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            app.yadwy.model.RegisterCustomerResponseDto(
                accountId = response.accountId,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
