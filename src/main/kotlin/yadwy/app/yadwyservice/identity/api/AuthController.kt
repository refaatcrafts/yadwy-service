package yadwy.app.yadwyservice.identity.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yadwy.app.yadwyservice.identity.application.IdentityService

@RestController
@RequestMapping("/api/identity/auth")
class AuthController(
    private val identityService: IdentityService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: AuthRequest) {
        identityService.register(request.phone, request.password)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): AuthResponse {
        val token = identityService.login(request.phone, request.password)
        return AuthResponse(token)
    }

    // DTOs
    data class AuthRequest(val phone: String, val password: String)
    data class AuthResponse(val token: String)
}