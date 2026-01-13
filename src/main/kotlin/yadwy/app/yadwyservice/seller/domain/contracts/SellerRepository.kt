package yadwy.app.yadwyservice.seller.domain.contracts

import yadwy.app.yadwyservice.seller.domain.models.Seller

interface SellerRepository {
    fun save(seller: Seller): Seller
    fun findById(sellerId: Long): Seller?
    fun findByAccountId(accountId: Long): Seller?
}
