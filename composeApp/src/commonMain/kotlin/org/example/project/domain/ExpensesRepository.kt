package org.example.project.domain


import kotlinx.coroutines.flow.Flow
import org.example.project.core.Resource
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

interface ExpensesRepository {
    fun getExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun editExpense(expense: Expense)
    suspend fun deleteExpense(id: Long)
    fun getCategories(): List<ExpenseCategory>
    fun getExpenseById(id: Long): Expense
}