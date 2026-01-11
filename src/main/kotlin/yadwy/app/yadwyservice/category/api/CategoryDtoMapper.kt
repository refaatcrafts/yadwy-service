package yadwy.app.yadwyservice.category.api

import app.yadwy.model.CategoryResponseDto
import app.yadwy.model.LocalizedDto
import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

fun CategoryResponse.toDto(): CategoryResponseDto = CategoryResponseDto(
    id = id,
    name = name.toDto(),
    slug = slug,
    description = description?.toDto(),
    imageUrl = imageUrl,
    parentId = parentId,
    children = children?.map { it.toDto() }
)

private fun Localized.toDto() = LocalizedDto(ar = ar, en = en.ifBlank { null })
