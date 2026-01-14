package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Component
class GetCart(
    private val cartRepository: CartRepository
) : UseCase<Long, CartResponse>() {

    override fun execute(request: Long): CartResponse {
        val cart = cartRepository.findByAccountId(request)
        return cart?.toResponse() ?: CartResponse(
            id = 0,
            accountId = request,
            items = emptyList(),
            total = Amount.ZERO
        )
    }
}
