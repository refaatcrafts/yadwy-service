package yadwy.app.yadwyservice.order.infrastructure.controllers

import app.yadwy.api.PlaceOrderApi
import app.yadwy.model.OrderResponseDto
import app.yadwy.model.PlaceOrderRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.order.application.models.OrderItemRequest
import yadwy.app.yadwyservice.order.application.models.PlaceOrderRequest
import yadwy.app.yadwyservice.order.application.usecases.PlaceOrder
import yadwy.app.yadwyservice.order.domain.models.OrderSource
import yadwy.app.yadwyservice.order.domain.models.PaymentMethod
import yadwy.app.yadwyservice.order.domain.models.ShippingAddress

@RestController
class PlaceOrderController(
    private val placeOrder: PlaceOrder
) : PlaceOrderApi {

    override fun placeOrder(placeOrderRequestDto: PlaceOrderRequestDto): ResponseEntity<OrderResponseDto> {
        val accountId = getAccountIdFromJwt()
        
        val request = PlaceOrderRequest(
            accountId = accountId,
            source = OrderSource.valueOf(placeOrderRequestDto.source.value),
            items = placeOrderRequestDto.items.map { item ->
                OrderItemRequest(
                    productId = item.productId,
                    sellerId = item.sellerId,
                    quantity = item.quantity
                )
            },
            shippingAddress = ShippingAddress(
                customerName = placeOrderRequestDto.shippingAddress.customerName,
                address = placeOrderRequestDto.shippingAddress.address,
                province = placeOrderRequestDto.shippingAddress.province,
                phone = placeOrderRequestDto.shippingAddress.phone,
                notes = placeOrderRequestDto.shippingAddress.notes
            ),
            paymentMethod = PaymentMethod.valueOf(placeOrderRequestDto.paymentMethod.value)
        )
        
        val response = placeOrder.execute(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response.toDto())
    }

    private fun getAccountIdFromJwt(): Long {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        return jwt.getClaim("id")
    }
}
