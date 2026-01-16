package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.api.PlaceOrderApi
import app.yadwy.model.OrderResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.order.application.models.PlaceOrderRequest
import yadwy.app.yadwyservice.order.application.usecases.PlaceOrder

@RestController
class PlaceOrderController(
    private val placeOrder: PlaceOrder
) : PlaceOrderApi {

    override fun placeOrder(): ResponseEntity<OrderResponseDto> {
        val accountId = getAccountIdFromJwt()
        val response = placeOrder.execute(PlaceOrderRequest(accountId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
