package yadwy.app.yadwyservice.product.application.usecases

import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.domain.models.Product

fun Product.toResponse() = ProductResponse(
    id = getId().id,
    sellerId = getSellerId(),
    name = getName(),
    description = getDescription(),
    images = getImages(),
    price = getPrice().value,
    compareAtPrice = getCompareAtPrice()?.value,
    categoryId = getCategoryId(),
    stock = getStock(),
    trackInventory = isTrackInventory(),
    visible = isVisible()
)
