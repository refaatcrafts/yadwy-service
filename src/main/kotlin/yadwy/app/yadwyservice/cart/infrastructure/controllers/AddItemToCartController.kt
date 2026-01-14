package yadwy.app.yadwyservice.cart.infrastructure.controllers

import app.yadwy.api.AddItemToCartApi
import app.yadwy.model.AddItemToCartRequestDto
import app.yadwy.model.CartResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.cart.application.models.AddItemToCartRequest
import yadwy.app.yadwyservice.cart.application.usecases.AddItemToCart

@RestController
class AddItemToCartController(
    private val addItemToCart: AddItemToCart
) : AddItemToCartApi {

    override fun addItemToCart(
        addItemToCartRequestDto: AddItemToCartRequestDto
    ): ResponseEntity<CartResponseDto> {
        val accountId = getAccountIdFromJwt()

        val request = AddItemToCartRequest(
            accountId = accountId,
            productId = addItemToCartRequestDto.productId,
            quantity = addItemToCartRequestDto.quantity
        )

        val response = addItemToCart.execute(request)
        return ResponseEntity.ok(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
