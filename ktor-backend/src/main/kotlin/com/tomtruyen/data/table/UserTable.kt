package com.tomtruyen.data.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val id = uuid("id").autoGenerate()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val salt = varchar("salt", 255)

    override val primaryKey = PrimaryKey(id)
}