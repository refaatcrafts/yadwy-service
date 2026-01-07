package yadwy.app.yadwyservice.identity.domain.exceptions

class InvalidNameException(val invalidValue: String, reason: String) :
    RuntimeException("The name '$invalidValue' is invalid. Reason: $reason")

class InvalidPhoneNumberException(phone: String) :
    RuntimeException("Phone number '$phone' is invalid. Phone cannot be empty")

class InvalidPasswordException(reason: String) :
    RuntimeException("Password is invalid. $reason")

class EncryptionFailedException :
    RuntimeException("Password encryption failed")

class InvalidCredentialsException : RuntimeException("Invalid phone number or password")

class AccountAlreadyExistsException(phone: String) :
    RuntimeException("Account with phone number $phone already exists")

class InvalidTokenException(reason: String) : RuntimeException("Invalid token: $reason")