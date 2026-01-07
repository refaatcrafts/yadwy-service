package yadwy.app.yadwyservice.identity.application.models

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)