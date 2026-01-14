package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.api.GetCartApi
import app.yadwy.model.CartResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.cart.application.usecases.GetCart

@RestController
class GetCartController(
    private val getCart: GetCart
) : GetCartApi {

    override fun getCart(): ResponseEntity<CartResponseDto> {
        val accountId = getAccountIdFromJwt()
        val response = getCart.execute(accountId)
        return ResponseEntity.ok(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
