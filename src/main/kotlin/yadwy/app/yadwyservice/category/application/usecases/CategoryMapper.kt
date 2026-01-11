package yadwy.app.yadwyservice.category.application.usecases

import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.category.domain.models.Category

fun Category.toResponse() = CategoryResponse(
    id = getId().id,
    name = getName(),
    slug = getSlug(),
    imageUrl = getImageUrl(),
    description = getDescription(),
    parentId = getParentId(),
    children = null
)
