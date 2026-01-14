package yadwy.app.yadwyservice.sharedkernel.domain.exceptions

class InvalidQuantityException(value: Int) : RuntimeException("Invalid quantity: $value. Quantity cannot be negative.")
