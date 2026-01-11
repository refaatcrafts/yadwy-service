package yadwy.app.yadwyservice.category.domain.models

import yadwy.app.yadwyservice.category.domain.exceptions.InvalidSlugException
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

@JvmInline
value class Slug private constructor(val value: String) : ValueObject {

    companion object {
        private val SLUG_PATTERN = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")

        fun of(slug: String): Slug {
            val normalized = slug.lowercase()
            if (!normalized.matches(SLUG_PATTERN)) {
                throw InvalidSlugException(slug)
            }
            return Slug(normalized)
        }
    }
}
