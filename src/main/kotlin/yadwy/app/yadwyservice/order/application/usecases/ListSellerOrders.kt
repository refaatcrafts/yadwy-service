package yadwy.app.yadwyservice.order.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

/**
 * Use case for listing all orders containing products from a specific seller.
 */
@Service
class ListSellerOrders(
    private val orderRepository: OrderRepository
) : UseCase<Long, List<OrderResponse>>() {

    override fun execute(request: Long): List<OrderResponse> {
        return orderRepository.findBySellerId(request).map { it.toResponse() }
    }
}
