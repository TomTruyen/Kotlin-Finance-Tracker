package com.tomtruyen.repositories

import com.tomtruyen.DatabaseFactory.dbQuery
import com.tomtruyen.data.model.User
import com.tomtruyen.data.model.toUser
import com.tomtruyen.data.table.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object UserRepository {
    suspend fun createUser(user: User) {
        dbQuery {
            UserTable.insert {
                it[email] = user.email
                it[password] = user.password
                it[salt] = user.salt
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .firstOrNull()
            .toUser()
    }
}