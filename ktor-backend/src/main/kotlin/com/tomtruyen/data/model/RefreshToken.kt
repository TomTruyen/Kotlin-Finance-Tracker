package com.tomtruyen.data.model

import com.tomtruyen.data.table.TokenTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

data class RefreshToken(
    val id: UUID,
    val userId: UUID,
    val refreshToken: String,
    val expiresAt: Long
)

fun ResultRow?.toRefreshToken(): RefreshToken? {
    if(this == null) {
        return null
    }

    return RefreshToken(
        id = this[TokenTable.id],
        userId = this[TokenTable.userId],
        refreshToken = this[TokenTable.refreshToken],
        expiresAt = this[TokenTable.expiresAt]
    )
}
