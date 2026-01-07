package yadwy.app.yadwyservice.identity.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Sequence
import org.springframework.data.relational.core.mapping.Table
import yadwy.app.yadwyservice.identity.domain.models.Role

@Table("accounts", schema = "identity")
data class AccountDbo(
    @Id
    @Sequence("accounts_id_seq", schema = "identity")
    val id: Long? = null,
    val name: String,
    val phoneNumber: String,
    val passwordHash: String,
    val roles: List<Role> = emptyList()
)