package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.api.ClearCartApi
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.cart.application.usecases.ClearCart

@RestController
class ClearCartController(
    private val clearCart: ClearCart
) : ClearCartApi {

    override fun clearCart(): ResponseEntity<Unit> {
        val accountId = getAccountIdFromJwt()
        clearCart.execute(accountId)
        return ResponseEntity.noContent().build()
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
