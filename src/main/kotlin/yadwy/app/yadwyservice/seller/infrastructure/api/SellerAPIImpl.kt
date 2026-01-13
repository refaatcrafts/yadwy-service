package yadwy.app.yadwyservice.seller.infrastructure.api

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.seller.api.SellerAPI
import yadwy.app.yadwyservice.seller.api.SellerDto
import yadwy.app.yadwyservice.seller.domain.contracts.SellerRepository

@Component
class SellerAPIImpl(
    private val sellerRepository: SellerRepository
) : SellerAPI {
    override fun findSellerByAccountId(accountId: Long): SellerDto? {
        val seller = sellerRepository.findByAccountId(accountId)
        return seller?.getId()?.id?.let { SellerDto(it) }
    }

    override fun sellerExists(sellerId: Long): Boolean {
        return sellerRepository.existsById(sellerId)
    }
}