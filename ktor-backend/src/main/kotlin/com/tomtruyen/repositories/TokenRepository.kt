package com.tomtruyen.repositories

import com.tomtruyen.DatabaseFactory.dbQuery
import com.tomtruyen.data.model.toRefreshToken
import com.tomtruyen.data.table.TokenTable
import com.tomtruyen.security.token.TokenPair
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.*

object TokenRepository {
    suspend fun findByRefreshToken(refreshToken: String) = dbQuery {
        TokenTable.select { TokenTable.refreshToken.eq(refreshToken) }
            .firstOrNull()
            .toRefreshToken()
    }

    suspend fun insertToken(tokenPair: TokenPair, userId: UUID, expiresAt: Long) = dbQuery {
        TokenTable.insert {
            it[TokenTable.userId] = userId
            it[refreshToken] = tokenPair.refreshToken
            it[TokenTable.expiresAt] = expiresAt
        }
    }

    suspend fun updateToken(tokenPair: TokenPair, userId: UUID, expiresAt: Long) = dbQuery {
        TokenTable.update({ TokenTable.userId.eq(userId) }) {
            it[refreshToken] = tokenPair.refreshToken
            it[TokenTable.expiresAt] = expiresAt
        }
    }

    suspend fun deleteToken(userId: UUID) = dbQuery {
        TokenTable.deleteWhere { TokenTable.userId.eq(userId) }
    }
}