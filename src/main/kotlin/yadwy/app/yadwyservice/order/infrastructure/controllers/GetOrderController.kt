package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.api.GetOrderApi
import app.yadwy.model.OrderResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.order.application.usecases.GetOrder

@RestController
class GetOrderController(
    private val getOrder: GetOrder
) : GetOrderApi {

    override fun getOrder(orderId: Long): ResponseEntity<OrderResponseDto> {
        val response = getOrder.execute(orderId)
        return ResponseEntity.ok(response.toDto())
    }
}
