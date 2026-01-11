package yadwy.app.yadwyservice.category.domain.exceptions

class CategoryNotFoundException(categoryId: Long) :
    RuntimeException("Category not found: $categoryId")

class ParentCategoryNotFoundException(parentId: Long) :
    RuntimeException("Parent category not found: $parentId")

class SlugAlreadyExistsException(slug: String) :
    RuntimeException("Slug already exists: $slug")

class InvalidSlugException(slug: String) :
    RuntimeException("Invalid slug format: $slug. Must be lowercase letters, numbers, and hyphens only.")

class CategoryHasChildrenException(categoryId: Long) :
    RuntimeException("Cannot delete category $categoryId: has child categories")
