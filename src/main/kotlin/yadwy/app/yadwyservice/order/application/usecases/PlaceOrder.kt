package yadwy.app.yadwyservice.order.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.application.models.PlaceOrderRequest
import yadwy.app.yadwyservice.order.domain.contracts.CartGateway
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.order.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.order.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.order.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.order.domain.models.Order
import yadwy.app.yadwyservice.order.domain.models.OrderLine
import yadwy.app.yadwyservice.order.domain.models.OrderSource
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Service
class PlaceOrder(
    private val orderRepository: OrderRepository,
    private val cartGateway: CartGateway,
    private val productGateway: ProductGateway
) : UseCase<PlaceOrderRequest, OrderResponse>() {

    override fun execute(request: PlaceOrderRequest): OrderResponse {
        // Build order lines grouped by seller, validating products and stock
        val linesBySeller = mutableMapOf<Long, MutableList<OrderLine>>()

        for (item in request.items) {
            val product = productGateway.getProductDetails(item.productId)
                ?: throw ProductNotFoundException(item.productId)

            if (item.quantity > product.stock) {
                throw InsufficientStockException(
                    item.productId,
                    item.quantity,
                    product.stock
                )
            }

            val orderLine = OrderLine(
                productId = product.productId,
                productName = product.name,
                unitPrice = product.price,
                quantity = Quantity.of(item.quantity)
            )

            linesBySeller.getOrPut(product.sellerId) { mutableListOf() }.add(orderLine)
        }

        for (item in request.items) {
            productGateway.decrementStock(item.productId, item.quantity)
        }

        val order = Order.create(
            accountId = request.accountId,
            linesBySeller = linesBySeller,
            shippingAddress = request.shippingAddress,
            paymentMethod = request.paymentMethod
        )
        val savedOrder = orderRepository.save(order)

        if (request.source == OrderSource.CART) {
            cartGateway.clearCart(request.accountId)
        }

        return savedOrder.toResponse()
    }
}
