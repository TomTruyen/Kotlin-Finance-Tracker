package com.tomtruyen.data.requests

import com.tomtruyen.data.model.CategoryType
import com.tomtruyen.data.model.TransactionType
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val amount: Double,
    val title: String,
    val details: String? = null,
    val transactionType: String,
    val type: String,
    val category: String? = null,
    val time: Long = System.currentTimeMillis(),
) {
    fun validate() {
        if(amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }

        if(title.isBlank()) {
            throw IllegalArgumentException("Title is required")
        }

        if(transactionType.isBlank()) {
            throw IllegalArgumentException("Transaction type is required")
        }

        if(TransactionType.values().none { it.type == transactionType }) {
            throw IllegalArgumentException("Transaction type is invalid. Options: single, recurring, scheduled")
        }

        if(type.isBlank()) {
            throw IllegalArgumentException("Type is required")
        }

        if(CategoryType.values().none { it.type == type }) {
            throw IllegalArgumentException("Type is invalid. Options: income, expense")
        }
    }
}