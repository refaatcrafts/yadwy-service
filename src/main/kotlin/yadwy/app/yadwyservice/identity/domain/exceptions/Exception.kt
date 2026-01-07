package yadwy.app.yadwyservice.identity.domain.exceptions

class InvalidNameException(val invalidValue: String, reason: String) :
    RuntimeException("The name '$invalidValue' is invalid. Reason: $reason")