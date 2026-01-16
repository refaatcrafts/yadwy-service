package yadwy.app.yadwyservice.order.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.application.models.PlaceOrderRequest
import yadwy.app.yadwyservice.order.domain.contracts.CartGateway
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.order.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.order.domain.exceptions.EmptyCartException
import yadwy.app.yadwyservice.order.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.order.domain.models.Order
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class PlaceOrder(
    private val orderRepository: OrderRepository,
    private val cartGateway: CartGateway,
    private val productGateway: ProductGateway
) : UseCase<PlaceOrderRequest, OrderResponse>() {

    override fun execute(request: PlaceOrderRequest): OrderResponse {
        val cartItems = cartGateway.getCartItems(request.accountId)
        
        if (cartItems.isEmpty()) {
            throw EmptyCartException(request.accountId)
        }

        // 2. Validate stock availability
        cartItems.firstOrNull { !it.hasSufficientStock() }?.let { item ->
            throw InsufficientStockException(
                item.productId,
                item.quantity.value,
                item.stock
            )
        }

        val order = Order.create(request.accountId, cartItems)

        cartItems.forEach { item ->
            productGateway.decrementStock(item.productId, item.quantity.value)
        }

        val savedOrder = orderRepository.save(order)
        cartGateway.clearCart(request.accountId)

        return savedOrder.toResponse()
    }
}
