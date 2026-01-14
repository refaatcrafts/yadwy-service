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

@Component
class AddItemToCart(
    private val cartRepository: CartRepository,
    private val productGateway: ProductGateway
) : UseCase<AddItemToCartRequest, CartResponse>() {

    override fun execute(request: AddItemToCartRequest): CartResponse {
        // Validate product exists
        if (!productGateway.productExists(request.productId)) {
            throw ProductNotFoundException(request.productId)
        }

        // Get product price for snapshot
        val unitPrice = productGateway.getProductPrice(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        // Validate stock availability
        val availableStock = productGateway.getAvailableStock(request.productId) ?: 0

        // Get or create cart
        val cart = cartRepository.findByAccountId(request.accountId)
            ?: Cart.create(request.accountId)

        // Check total quantity if item already in cart
        val existingItem = cart.getItems().find { it.getProductId() == request.productId }
        val totalQuantity = (existingItem?.getQuantity() ?: 0) + request.quantity
        if (totalQuantity > availableStock) {
            throw InsufficientStockException(request.productId, totalQuantity, availableStock)
        }

        cart.addItem(request.productId, request.quantity, unitPrice)
        val saved = cartRepository.save(cart)
        return saved.toResponse()
    }
}
