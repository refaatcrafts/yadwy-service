package yadwy.app.yadwyservice.identity.domain.contracts

import yadwy.app.yadwyservice.identity.domain.models.Account

interface JWTGenerator {
    fun generateAccessToken(account: Account): String
    fun generateRefreshToken(account: Account): String
}