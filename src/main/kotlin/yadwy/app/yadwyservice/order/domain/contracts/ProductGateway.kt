package yadwy.app.yadwyservice.order.domain.contracts

import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

/**
 * DTO containing product details needed for order creation.
 */
data class ProductDetailsDto(
    val productId: Long,
    val sellerId: Long,
    val name: Localized,
    val price: Amount,
    val stock: Int
)

/**
 * Gateway contract for product operations.
 * Used by the Order module to retrieve product details and manage stock.
 */
interface ProductGateway {
    /**
     * Retrieves product details including seller info, price, and stock.
     */
    fun getProductDetails(productId: Long): ProductDetailsDto?

    /**
     * Decrements product stock after order placement.
     */
    fun decrementStock(productId: Long, quantity: Int)

    /**
     * Checks if a product exists.
     */
    fun existsById(productId: Long): Boolean
}
