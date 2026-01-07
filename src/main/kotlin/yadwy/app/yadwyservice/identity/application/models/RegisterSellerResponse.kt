package yadwy.app.yadwyservice.identity.application.models

data class RegisterSellerResponse(
    val accountId: Long,
    val accessToken: String,
    val refreshToken: String
)