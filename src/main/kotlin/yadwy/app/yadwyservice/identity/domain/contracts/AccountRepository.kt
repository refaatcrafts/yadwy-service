package yadwy.app.yadwyservice.identity.domain.contracts

import yadwy.app.yadwyservice.identity.domain.models.Account

interface AccountRepository{
    fun save(account: Account): Account
    fun findByPhoneNumber(phone: String): Account?
}