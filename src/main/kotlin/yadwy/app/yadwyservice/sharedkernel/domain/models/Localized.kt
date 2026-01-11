package yadwy.app.yadwyservice.sharedkernel.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

/**
 * Value object for localized text content supporting Arabic and English.
 * At least one language must be provided (non-blank).
 */
data class Localized(
    val ar: String,
    val en: String
) : ValueObject {
    init {
        require(ar.isNotBlank() || en.isNotBlank()) {
            "At least one language (ar or en) must be provided"
        }
    }

    companion object {
        fun of(ar: String = "", en: String = ""): Localized = Localized(ar, en)
    }
}
