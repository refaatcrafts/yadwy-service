package yadwy.app.yadwyservice.identity.application.models

data class RegisterSellerRequest(
    val name: String,
    val phoneNumber: String,
    val password: String
)