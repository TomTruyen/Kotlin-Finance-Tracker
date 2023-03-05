package com.tomtruyen.data.table

import org.jetbrains.exposed.sql.Table

object CategoryTable: Table() {
    val id = uuid("id").autoGenerate()
    val name = varchar("name", 255)
    val type = varchar("type", 255)
    val userId = reference("userId", UserTable.id)

    override val primaryKey = PrimaryKey(id)
}