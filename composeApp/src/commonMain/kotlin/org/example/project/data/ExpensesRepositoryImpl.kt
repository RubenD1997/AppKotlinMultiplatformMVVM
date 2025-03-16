package org.example.project.data

import org.example.project.domain.ExpensesRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

class ExpensesRepositoryImpl(private val expenseManager: ExpenseManager) : ExpensesRepository {

    override fun getExpenses(): List<Expense> {
        return expenseManager.fakeExpenses
    }

    override fun addExpense(expense: Expense) {
        expenseManager.addNewExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        expenseManager.editExpense(expense)
    }

    override fun deleteExpense(expense: Expense) {
        expenseManager.deleteExpense(expense)
    }

    override fun getCategories(): List<ExpenseCategory> {
        return expenseManager.getCategories()
    }
}