package yadwy.app.yadwyservice.order.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.cart.api.CartAPI
import yadwy.app.yadwyservice.order.domain.contracts.CartGateway

@Service
class CartGatewayImpl(
    private val cartAPI: CartAPI
) : CartGateway {

    override fun clearCart(accountId: Long) {
        cartAPI.clearCart(accountId)
    }
}
