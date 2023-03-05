package com.tomtruyen.data.model

import com.tomtruyen.data.table.CategoryTable
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

@Serializable
data class Category(
    @Contextual
    var id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String
)

enum class CategoryType(val type: String) {
    INCOME("income"),
    EXPENSE("expense")
}

fun ResultRow?.toCategory(): Category? {
    if(this == null) {
        return null
    }

    return Category(
        id = this[CategoryTable.id],
        name = this[CategoryTable.name],
        type = this[CategoryTable.type]
    )
}
