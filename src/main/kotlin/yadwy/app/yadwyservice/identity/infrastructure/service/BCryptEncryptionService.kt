package yadwy.app.yadwyservice.identity.infrastructure.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.identity.domain.contracts.EncryptionService

@Component
class BCryptEncryptionService : EncryptionService {
    private val encoder = BCryptPasswordEncoder()

    override fun encrypt(raw: String) = encoder.encode(raw)
    override fun matches(raw: String, hashed: String) = encoder.matches(raw, hashed)
}