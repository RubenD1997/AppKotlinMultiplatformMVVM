package org.example.project.data

import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

object ExpenseManager {
    private var currentId = 1L

    fun addNewExpense(expense: Expense) {
        fakeExpenses.add(expense.copy(id = currentId++))
    }

    fun editExpense(expense: Expense) {
        val index = fakeExpenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            fakeExpenses[index] = fakeExpenses[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )
        }
    }

    fun getCategories(): List<ExpenseCategory> {
        return ExpenseCategory.entries
    }

    fun deleteExpense(expense: Expense) {
        fakeExpenses.remove(expense)
    }

    val fakeExpenses = mutableListOf(
        Expense(
            id = currentId++,
            amount = 100.0,
            category = ExpenseCategory.GROCERIES,
            description = "Groceries buy"
        ),
        Expense(
            id = currentId++,
            amount = 200.0,
            category = ExpenseCategory.HOUSE,
            description = "Home buy"
        ),
        Expense(
            id = currentId++,
            amount = 21100.0,
            category = ExpenseCategory.CAR,
            description = "Audi A1"
        ),
        Expense(
            id = currentId++,
            amount = 1100.0,
            category = ExpenseCategory.PARTY,
            description = "Party buy"
        ),
        Expense(
            id = currentId++,
            amount = 2100.0,
            category = ExpenseCategory.COFFEE,
            description = "StarBucks buy"
        ),
        Expense(
            id = currentId++,
            amount = 100.0,
            category = ExpenseCategory.OTHER,
            description = "Services Pay"
        )
    )
}