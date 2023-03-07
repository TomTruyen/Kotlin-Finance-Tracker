package com.tomtruyen.repositories

import com.tomtruyen.DatabaseFactory.dbQuery
import com.tomtruyen.data.model.Transaction
import com.tomtruyen.data.model.TransactionFilter
import com.tomtruyen.data.model.toTransaction
import com.tomtruyen.data.table.TransactionTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object TransactionRepository {
    suspend fun createTransaction(transaction: Transaction) = dbQuery {
        TransactionTable.insert {
            it[amount] = transaction.amount
            it[title] = transaction.title
            it[details] = transaction.details
            it[transactionType] = transaction.transactionType
            it[type] = transaction.type
            it[category] = transaction.category
            it[time] = transaction.time
            it[userId] = transaction.userId
        }
    }

    suspend fun updateTransaction(transaction: Transaction) = dbQuery {
        TransactionTable.update({ TransactionTable.id.eq(transaction.id) }) {
            it[amount] = transaction.amount
            it[title] = transaction.title
            it[details] = transaction.details
            it[transactionType] = transaction.transactionType
            it[type] = transaction.type
            it[category] = transaction.category
            it[time] = transaction.time
        }
    }

    suspend fun deleteTransaction(id: UUID) {
        dbQuery {
            TransactionTable.deleteWhere { TransactionTable.id.eq(id) }
        }
    }

    suspend fun findAllByFilter(filter: TransactionFilter, userId: UUID) = dbQuery {
        var query = TransactionTable.select { TransactionTable.userId.eq(userId) }

        if(filter.startTime != null) {
            query = query.andWhere { TransactionTable.time.greaterEq(filter.startTime) }
        }

        if(filter.endTime != null) {
            query = query.andWhere { TransactionTable.time.lessEq(filter.endTime) }
        }

        if(filter.type != null) {
            query = query.andWhere { TransactionTable.type.eq(filter.type) }
        }

        if(filter.transactionType != null) {
            query = query.andWhere { TransactionTable.transactionType.eq(filter.transactionType) }
        }

        if(filter.category != null) {
            query = query.andWhere { TransactionTable.category.eq(filter.category) }
        }

        query.map {
            it.toTransaction()
        }
    }
}