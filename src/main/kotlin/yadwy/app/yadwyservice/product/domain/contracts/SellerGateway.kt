package yadwy.app.yadwyservice.product.domain.contracts

import yadwy.app.yadwyservice.seller.api.SellerDto

interface SellerGateway {
    fun findSellerIdByAccountId(accountId: Long): SellerDto?
    fun sellerExists(sellerId: Long): Boolean
}
