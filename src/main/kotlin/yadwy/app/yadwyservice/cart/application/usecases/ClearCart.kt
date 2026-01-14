package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class ClearCart(
    private val cartRepository: CartRepository
) : UseCase<Long, Unit>() {

    override fun execute(request: Long) {
        val cart = cartRepository.findByAccountId(request) ?: return
        cart.clear()
        cartRepository.save(cart)
    }
}
