package yadwy.app.yadwyservice.identity.application.models

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)