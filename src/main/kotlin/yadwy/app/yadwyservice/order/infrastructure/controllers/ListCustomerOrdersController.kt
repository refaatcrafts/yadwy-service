package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.api.ListCustomerOrdersApi
import app.yadwy.model.OrderResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.order.application.usecases.ListCustomerOrders

@RestController
class ListCustomerOrdersController(
    private val listCustomerOrders: ListCustomerOrders
) : ListCustomerOrdersApi {

    override fun listCustomerOrders(): ResponseEntity<List<OrderResponseDto>> {
        val accountId = getAccountIdFromJwt()
        val orders = listCustomerOrders.execute(accountId)
        return ResponseEntity.ok(orders.map { it.toDto() })
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
