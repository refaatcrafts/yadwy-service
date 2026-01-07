package yadwy.app.yadwyservice.identity.domain.contracts

import yadwy.app.yadwyservice.identity.domain.models.Account

interface TokenProvider {
    fun createAccessToken(account: Account): String
    fun createRefreshToken(account: Account): String
}