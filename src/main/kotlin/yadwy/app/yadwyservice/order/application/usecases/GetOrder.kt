package yadwy.app.yadwyservice.order.application.usecases

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.application.models.OrderResponse
import yadwy.app.yadwyservice.order.domain.contracts.OrderRepository
import yadwy.app.yadwyservice.order.domain.exceptions.OrderNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Service
class GetOrder(
    private val orderRepository: OrderRepository
) : UseCase<Long, OrderResponse>() {

    override fun execute(request: Long): OrderResponse {
        val order = orderRepository.findById(request)
            ?: throw OrderNotFoundException(request)
        return order.toResponse()
    }
}
