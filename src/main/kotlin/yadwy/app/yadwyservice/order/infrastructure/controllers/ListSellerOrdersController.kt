package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.api.ListSellerOrdersApi
import app.yadwy.model.OrderResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.order.application.usecases.ListSellerOrders
import yadwy.app.yadwyservice.seller.api.SellerAPI

@RestController
class ListSellerOrdersController(
    private val listSellerOrders: ListSellerOrders,
    private val sellerAPI: SellerAPI
) : ListSellerOrdersApi {

    override fun listSellerOrders(): ResponseEntity<List<OrderResponseDto>> {
        val accountId = getAccountIdFromJwt()
        val seller = sellerAPI.findSellerByAccountId(accountId)
            ?: return ResponseEntity.ok(emptyList())
        
        val orders = listSellerOrders.execute(seller.id)
        return ResponseEntity.ok(orders.map { it.toDto() })
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
