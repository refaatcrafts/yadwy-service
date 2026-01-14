package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.api.UpdateCartItemQuantityApi
import app.yadwy.model.CartResponseDto
import app.yadwy.model.UpdateCartItemQuantityRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.cart.application.models.UpdateCartItemQuantityRequest
import yadwy.app.yadwyservice.cart.application.usecases.UpdateCartItemQuantity

@RestController
class UpdateCartItemQuantityController(
    private val updateCartItemQuantity: UpdateCartItemQuantity
) : UpdateCartItemQuantityApi {

    override fun updateCartItemQuantity(
        productId: Long,
        updateCartItemQuantityRequestDto: UpdateCartItemQuantityRequestDto
    ): ResponseEntity<CartResponseDto> {
        val accountId = getAccountIdFromJwt()

        val request = UpdateCartItemQuantityRequest(
            accountId = accountId,
            productId = productId,
            quantity = updateCartItemQuantityRequestDto.quantity
        )

        val response = updateCartItemQuantity.execute(request)
        return ResponseEntity.ok(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
