package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.AddItemToCartRequest
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.cart.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.cart.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.cart.domain.models.Cart
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Component
class AddItemToCart(
    private val cartRepository: CartRepository,
    private val productGateway: ProductGateway
) : UseCase<AddItemToCartRequest, CartResponse>() {

    override fun execute(request: AddItemToCartRequest): CartResponse {
        if (!productGateway.productExists(request.productId)) {
            throw ProductNotFoundException(request.productId)
        }

        val unitPrice = productGateway.getProductPrice(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        val availableStock = productGateway.getAvailableStock(request.productId) ?: 0

        val cart = cartRepository.findByAccountId(request.accountId)
            ?: Cart.create(request.accountId)

        val existingItem = cart.getItems().find { it.getProductId() == request.productId }
        val totalQuantity = (existingItem?.getQuantity()?.value ?: 0) + request.quantity
        if (totalQuantity > availableStock) {
            throw InsufficientStockException(request.productId, totalQuantity, availableStock)
        }

        cart.addItem(request.productId, Quantity.of(request.quantity), unitPrice)
        val saved = cartRepository.save(cart)
        return saved.toResponse()
    }
}
