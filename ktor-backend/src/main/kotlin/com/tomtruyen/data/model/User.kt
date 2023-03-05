package com.tomtruyen.data.model

import com.tomtruyen.data.table.UserTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val password: String,
    val salt: String
)

fun ResultRow?.toUser(): User? {
    if(this == null) {
        return null
    }

    return User(
        id = this[UserTable.id],
        email = this[UserTable.email],
        password = this[UserTable.password],
        salt = this[UserTable.salt]
    )
}