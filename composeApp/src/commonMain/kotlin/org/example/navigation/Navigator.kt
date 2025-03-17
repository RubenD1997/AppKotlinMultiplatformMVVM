package org.example.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import org.example.project.GetColorsTheme
import org.example.project.presentation.ExpensesViewModel
import org.example.project.ui.ExpenseDetailScreen
import org.example.project.ui.ExpensesScreen
import org.koin.core.parameter.parametersOf

@Composable
fun Navigation(navigator: Navigator) {
    val colors = GetColorsTheme()
    val viewModel = koinViewModel(ExpensesViewModel::class) {
        parametersOf()
    }
    NavHost(
        modifier = Modifier.background(colors.bacGroundColor),
        navigator = navigator,
        initialRoute = "/home"
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpensesScreen(uiState = uiState) {
                navigator.navigate("/addExpenses/${it.id}")
            }
        }
        scene(route = "/addExpenses/{id}?") { backStackEntry ->
            val idFromPath: Long? = backStackEntry.path<Long>("id")
            val expense = idFromPath?.let { id -> viewModel.getExpenseById(id) }
            ExpenseDetailScreen(expense, categoryList = viewModel.getCategories()) { expenseItem ->
                if (expense == null) {
                    viewModel.addExpense(expenseItem)
                } else {
                    viewModel.editExpense(expenseItem)
                }
                navigator.popBackStack()
            }
        }
    }
}

