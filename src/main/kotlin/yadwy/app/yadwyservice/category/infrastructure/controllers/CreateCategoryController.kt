package yadwy.app.yadwyservice.category.infrastructure.controllers

import app.yadwy.api.CreateCategoryApi
import app.yadwy.model.CategoryResponseDto
import app.yadwy.model.CreateCategoryRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.category.application.models.CreateCategoryRequest
import yadwy.app.yadwyservice.category.application.usecases.CreateCategory

@RestController
class CreateCategoryController(
    private val createCategory: CreateCategory
) : CreateCategoryApi {

    override fun createCategory(createCategoryRequestDto: CreateCategoryRequestDto): ResponseEntity<CategoryResponseDto> {
        val request = CreateCategoryRequest(
            name = createCategoryRequestDto.name.toDomain(),
            slug = createCategoryRequestDto.slug,
            imageUrl = createCategoryRequestDto.imageUrl,
            description = createCategoryRequestDto.description?.toDomain(),
            parentId = createCategoryRequestDto.parentId
        )

        val response = createCategory.execute(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response.toDto())
    }
}
