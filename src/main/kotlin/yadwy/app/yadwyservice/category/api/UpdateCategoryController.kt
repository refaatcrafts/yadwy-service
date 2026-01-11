package yadwy.app.yadwyservice.category.api

import app.yadwy.api.UpdateCategoryApi
import app.yadwy.model.CategoryResponseDto
import app.yadwy.model.LocalizedDto
import app.yadwy.model.UpdateCategoryRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.category.application.models.UpdateCategoryRequest
import yadwy.app.yadwyservice.category.application.usecases.UpdateCategory
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

@RestController
class UpdateCategoryController(
    private val updateCategory: UpdateCategory
) : UpdateCategoryApi {

    override fun updateCategory(id: Long, updateCategoryRequestDto: UpdateCategoryRequestDto): ResponseEntity<CategoryResponseDto> {
        val request = UpdateCategoryRequest(
            categoryId = id,
            name = updateCategoryRequestDto.name?.toDomain(),
            imageUrl = updateCategoryRequestDto.imageUrl,
            description = updateCategoryRequestDto.description?.toDomain()
        )

        val response = updateCategory.execute(request)

        return ResponseEntity.ok(response.toDto())
    }
}

private fun LocalizedDto.toDomain() = Localized(ar = ar, en = en ?: "")
