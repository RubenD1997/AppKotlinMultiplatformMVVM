package org.example.project.data


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.example.db.AppDatabase
import org.example.project.core.Resource
import org.example.project.core.Resource.Failure
import org.example.project.domain.ExpensesRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import org.example.project.model.NetworkExpense
import org.example.project.model.toExpense

class ExpensesRepositoryImpl(
    private val expenseManager: ExpenseManager,
    database: AppDatabase,
    private val client: HttpClient
) : ExpensesRepository {

    private val queries = database.expensesDbQueries


    override fun getExpenses(): Flow<List<Expense>> {
        return flow {
            val localExpenses = queries.selectAll().executeAsList()
            if (localExpenses.isEmpty()) {
                val networkResponse =
                    client.get("expenses").body<List<NetworkExpense>>().map { it.toExpense() }
                if (queries.selectAll().executeAsList().isEmpty()) {
                    networkResponse.forEach {
                        queries.insert(it.amount, it.category.name, it.description)
                    }
                }
            } else {
                queries.selectAll().asFlow().mapToList(Dispatchers.IO).map {
                    it.map { expense ->
                        Expense(
                            expense.id,
                            expense.amount,
                            ExpenseCategory.valueOf(expense.categoryName),
                            expense.description
                        )
                    }
                }.collect {
                    emit(it)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addExpense(expense: Expense) {
        try {
            client.post("expenses") {
                setBody(
                    NetworkExpense(
                        amount = expense.amount,
                        categoryName = expense.category.name,
                        description = expense.description
                    )
                )
            }
        } catch (e: Exception) {
            println(e.message.toString())
            queries.transaction {
                queries.insert(expense.amount, expense.category.name, expense.description)
            }
        }
    }

    override suspend fun editExpense(expense: Expense) {
        try {
            client.put("expenses/${expense.id}") {
                setBody(
                    NetworkExpense(
                        amount = expense.amount,
                        categoryName = expense.category.name,
                        description = expense.description
                    )
                )
            }
        } catch (e: Exception) {
            println(e.message.toString())
            queries.transaction {
                queries.update(
                    expense.amount,
                    expense.category.name,
                    expense.description,
                    expense.id
                )
            }
        }
    }

    override suspend fun deleteExpense(id: Long) {
        try {
            client.delete("expenses/${id}")
        } catch (e: Exception) {
            println(e.message.toString())
            queries.transaction {
                queries.delete(id)
            }
        }
    }

    override fun getCategories(): List<ExpenseCategory> {
        return expenseManager.getCategories()
    }

    override fun getExpenseById(id: Long): Expense {
        return queries.selectById(id).executeAsList().map {
            Expense(
                it.id,
                it.amount,
                ExpenseCategory.valueOf(it.categoryName),
                it.description
            )
        }.first()
    }
}