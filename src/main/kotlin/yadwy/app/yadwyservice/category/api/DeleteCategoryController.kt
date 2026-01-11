package yadwy.app.yadwyservice.category.api

import app.yadwy.api.DeleteCategoryApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.category.application.usecases.DeleteCategory

@RestController
class DeleteCategoryController(
    private val deleteCategory: DeleteCategory
) : DeleteCategoryApi {

    override fun deleteCategory(id: Long): ResponseEntity<Unit> {
        deleteCategory.execute(id)
        return ResponseEntity.noContent().build()
    }
}
