package com.tomtruyen.data.table

import org.jetbrains.exposed.sql.Table

object TokenTable: Table() {
    val id = uuid("id").autoGenerate()
    val refreshToken = varchar("refreshToken", 255)
    val userId = reference("userId", UserTable.id)
    val expiresAt = long("expiresAt")

    override val primaryKey = PrimaryKey(id)
}