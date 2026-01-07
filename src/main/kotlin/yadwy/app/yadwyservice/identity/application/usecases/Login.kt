package yadwy.app.yadwyservice.identity.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.identity.application.models.LoginRequest
import yadwy.app.yadwyservice.identity.application.models.LoginResponse
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class Login : UseCase<LoginRequest, LoginResponse>() {

    override fun execute(request: LoginRequest): LoginResponse {
        TODO("Not yet implemented")
    }
}