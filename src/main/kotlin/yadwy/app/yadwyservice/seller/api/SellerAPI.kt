package yadwy.app.yadwyservice.seller.api

interface SellerAPI {
    fun findSellerByAccountId(accountId: Long): SellerDto?
    fun sellerExists(sellerId: Long): Boolean
}