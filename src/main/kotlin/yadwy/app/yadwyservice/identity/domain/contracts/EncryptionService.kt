package yadwy.app.yadwyservice.identity.domain.contracts

interface EncryptionService {
    fun encrypt(raw: String): String?
    fun matches(raw: String, hashed: String): Boolean
}