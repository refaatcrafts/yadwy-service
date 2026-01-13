package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.model.LocalizedDto
import app.yadwy.model.ProductResponseDto
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

fun ProductResponse.toDto(): ProductResponseDto = ProductResponseDto(
    id = id,
    sellerId = sellerId,
    name = name.toDto(),
    description = description?.toDto(),
    images = images,
    price = price.toDouble(),
    compareAtPrice = compareAtPrice?.toDouble(),
    categoryId = categoryId,
    stock = stock,
    trackInventory = trackInventory,
    visible = visible
)

fun Localized.toDto() = LocalizedDto(ar = ar, en = en.ifBlank { null })

fun LocalizedDto.toDomain() = Localized(ar = ar, en = en ?: "")
