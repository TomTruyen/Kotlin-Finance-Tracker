package com.tomtruyen.data.table

import org.jetbrains.exposed.sql.Table

object TransactionTable: Table() {
    val id = uuid("id").autoGenerate()
    val amount = double("amount")
    val title = varchar("title", 255)
    val details = varchar("details", 3000).nullable()
    val transactionType = varchar("transactionType", 255)
    val type = varchar("type", 255)
    val category = varchar("category", 255).nullable()
    val time = long("time")
    val userId = reference("userId", UserTable.id)

    override val primaryKey = PrimaryKey(id)
}