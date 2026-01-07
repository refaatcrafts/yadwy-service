package yadwy.app.yadwyservice.identity.application.models

data class RegisterCustomerResponse(
    val accountId: Long,
    val accessToken: String,
    val refreshToken: String
)