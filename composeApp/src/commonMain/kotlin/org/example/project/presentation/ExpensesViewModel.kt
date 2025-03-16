package org.example.project.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.domain.ExpensesRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)

class ExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState = _uiState.asStateFlow()
    private val allExpenses = expensesRepository.getExpenses()

    init {
        getAllExpenses()
    }

    private fun updateState() {
        _uiState.update { state ->
            state.copy(expenses = allExpenses, total = allExpenses.sumOf { it.amount })
        }
    }

    private fun getAllExpenses() {
        viewModelScope.launch {
            updateState()
        }
    }

    fun getCategories(): List<ExpenseCategory> {
        return expensesRepository.getCategories()
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expensesRepository.deleteExpense(expense)
            updateState()
        }
    }

    fun getExpenseById(id: Long): Expense {
        return allExpenses.first { it.id == id }
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            expensesRepository.editExpense(expense)
            updateState()
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expensesRepository.addExpense(expense)
            updateState()
        }
    }

}