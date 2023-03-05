package com.tomtruyen.repositories

import com.tomtruyen.DatabaseFactory.dbQuery
import com.tomtruyen.data.model.Category
import com.tomtruyen.data.model.toCategory
import com.tomtruyen.data.table.CategoryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object CategoryRepository {
    suspend fun createCategory(category: Category, userId: UUID) = dbQuery {
        CategoryTable.insert {
            it[name] = category.name
            it[type] = category.type
            it[this.userId] = userId
        }
    }

    suspend fun updateCategory(category: Category) = dbQuery {
        CategoryTable.update({ CategoryTable.id.eq(category.id) }) {
            it[name] = category.name
            it[type] = category.type
        }
    }

    suspend fun deleteCategory(id: UUID) {
        dbQuery {
            CategoryTable.deleteWhere { CategoryTable.id.eq(id) }
        }
    }

    suspend fun findAllByType(userId: UUID, type: String) = dbQuery {
        CategoryTable.select { CategoryTable.userId.eq(userId) }
            .andWhere { CategoryTable.type.eq(type) }
            .map { it.toCategory() }
    }
}