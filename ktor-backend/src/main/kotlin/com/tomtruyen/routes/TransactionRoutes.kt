package com.tomtruyen.routes

import com.tomtruyen.data.model.Transaction
import com.tomtruyen.data.model.TransactionFilter
import com.tomtruyen.data.requests.TransactionRequest
import com.tomtruyen.data.responses.ErrorResponse
import com.tomtruyen.data.table.TransactionTable
import com.tomtruyen.repositories.TransactionRepository
import com.tomtruyen.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createTransaction() {
    post("transactions") {
        val request = call.receiveNullable<TransactionRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        try {
            request.validate()
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: ""))
            return@post
        }

        val userId = call.getUserId()
        val transaction = Transaction(
            amount = request.amount,
            title = request.title,
            details = request.details,
            transactionType = request.transactionType,
            type = request.type,
            category = request.category,
            time = request.time,
            userId = userId
        )

        TransactionRepository.createTransaction(transaction).let {
            it.resultedValues?.firstOrNull()?.let { row ->
                transaction.id = row[TransactionTable.id]
            }
        }

        call.respond(HttpStatusCode.OK, transaction)
    }
}

fun Route.updateTransaction() {
    put("transactions/{id}") {
        val request = call.receiveNullable<TransactionRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }

        val transactionId = call.parameters["id"]
        if(transactionId.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Transaction id is required"))
            return@put
        }

        try {
            request.validate()
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: ""))
            return@put
        }

        val userId = call.getUserId()
        val transaction = Transaction(
            id = UUID.fromString(transactionId),
            amount = request.amount,
            title = request.title,
            details = request.details,
            transactionType = request.transactionType,
            type = request.type,
            category = request.category,
            time = request.time,
            userId = userId
        )

        TransactionRepository.updateTransaction(transaction)

        call.respond(HttpStatusCode.OK, transaction)
    }
}

fun Route.deleteTransaction() {
    delete("transactions/{id}") {
        val transactionId = call.parameters["id"]

        if(transactionId.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Transaction id is required"))
            return@delete
        }

        TransactionRepository.deleteTransaction(UUID.fromString(transactionId))

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.getTransactions() {
    get("transactions") {
        val filter = TransactionFilter.fromQueryParameters(call.request.queryParameters)
        val userId = call.getUserId()

        val transactions = TransactionRepository.findAllByFilter(filter, userId)

        call.respond(HttpStatusCode.OK, transactions)
    }
}