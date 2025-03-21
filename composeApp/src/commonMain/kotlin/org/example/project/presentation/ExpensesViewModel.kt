package org.example.project.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.core.Resource
import org.example.project.core.Resource.Failure
import org.example.project.core.Resource.Loading
import org.example.project.core.Resource.Success
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

    val expenses: StateFlow<Resource<ExpensesUiState>> = expensesRepository.getExpenses().map {
        Success(ExpensesUiState(it, it.sumOf { expense -> expense.amount }))
    }.catch { Failure(it) }.stateIn(viewModelScope, SharingStarted.Lazily, Loading)

    fun getCategories(): List<ExpenseCategory> {
        return expensesRepository.getCategories()
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            expensesRepository.deleteExpense(id)
        }
    }

    fun getExpenseById(id: Long): Expense {
        return expensesRepository.getExpenseById(id)
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            expensesRepository.editExpense(expense)
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expensesRepository.addExpense(expense)
        }
    }

}