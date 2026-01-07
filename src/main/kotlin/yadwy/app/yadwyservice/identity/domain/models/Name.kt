package yadwy.app.yadwyservice.identity.domain.models

import yadwy.app.yadwyservice.identity.domain.exceptions.InvalidNameException

@JvmInline
value class Name(val value: String) {
    init {
        if (value.isBlank()) throw InvalidNameException(value, "Cannot be blank")
        if (value.length !in 2..50) throw InvalidNameException(value, "Length must be 2-50")
        if (!value.matches(regex)) throw InvalidNameException(value, "Only letters allowed")
    }

    companion object {
        private val regex = Regex("^[a-zA-Z\\s]+$")
    }
}