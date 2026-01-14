package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.api.RemoveItemFromCartApi
import app.yadwy.model.CartResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.cart.application.models.RemoveItemFromCartRequest
import yadwy.app.yadwyservice.cart.application.usecases.RemoveItemFromCart

@RestController
class RemoveItemFromCartController(
    private val removeItemFromCart: RemoveItemFromCart
) : RemoveItemFromCartApi {

    override fun removeItemFromCart(productId: Long): ResponseEntity<CartResponseDto> {
        val accountId = getAccountIdFromJwt()

        val request = RemoveItemFromCartRequest(
            accountId = accountId,
            productId = productId
        )

        val response = removeItemFromCart.execute(request)
        return ResponseEntity.ok(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
