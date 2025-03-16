package org.example.project.domain

import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

interface ExpensesRepository {
    fun getExpenses(): List<Expense>
    fun addExpense(expense: Expense)
    fun editExpense(expense: Expense)
    fun deleteExpense(expense: Expense)
    fun getCategories(): List<ExpenseCategory>
}