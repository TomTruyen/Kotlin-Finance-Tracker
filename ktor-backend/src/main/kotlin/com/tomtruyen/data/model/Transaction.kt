package com.tomtruyen.data.model

import com.tomtruyen.data.table.TransactionTable
import io.ktor.http.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

@Serializable
data class Transaction(
    @Contextual
    var id: UUID = UUID.randomUUID(),
    val amount: Double,
    val title: String,
    val details: String?,
    val transactionType: String, // single, recurring, scheduled
    val type: String, // income, expense
    val category: String?, // food, drink, etc
    val time: Long,
    @Contextual
    val userId: UUID
)

@Serializable
data class TransactionFilter(
    val startTime: Long? = null,
    val endTime: Long? = null,
    val type: String? = null,
    val category: String? = null,
    val transactionType: String? = null,
) {
    companion object {
        fun fromQueryParameters(parameters: Parameters): TransactionFilter {
            val startTime = parameters["startTime"]?.toLongOrNull()
            val endTime = parameters["endTime"]?.toLongOrNull()
            val type = parameters["type"]
            val category = parameters["category"]
            val transactionType = parameters["transactionType"]

            return TransactionFilter(
                startTime = startTime,
                endTime = endTime,
                type = type,
                category = category,
                transactionType = transactionType
            )
        }
    }
}

enum class TransactionType(val type: String) {
    SINGLE("single"),
    RECURRING("recurring"),
    SCHEDULED("scheduled"),
}

fun ResultRow?.toTransaction(): Transaction? {
    if(this == null) {
        return null
    }

    return Transaction(
        id = this[TransactionTable.id],
        amount = this[TransactionTable.amount],
        title = this[TransactionTable.title],
        details = this[TransactionTable.details],
        transactionType = this[TransactionTable.transactionType],
        type = this[TransactionTable.type],
        category = this[TransactionTable.category],
        time = this[TransactionTable.time],
        userId = this[TransactionTable.userId]
    )
}