import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilExactlyOneExists
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.example.project.GetColorsTheme
import org.example.project.core.Resource.Failure
import org.example.project.core.Resource.Loading
import org.example.project.core.Resource.Success
import org.example.project.data.ExpenseManager
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import org.example.project.presentation.ExpensesUiState
import org.example.project.ui.ExpenseDetailScreen
import org.example.project.ui.ExpenseItemActions
import org.example.project.ui.ExpensesScreen
import org.example.project.utils.EXPENSE_DETAIL_TEST_TAG
import org.example.project.utils.EXPENSE_EMPTY_SUCCESS_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_CLICK_ITEM_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_CONTENT_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_ERROR_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_LOADING_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_SUCCESS_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_SUCCESS_TOTAL_TEST_TAG
import kotlin.test.Test

class ExpenseScreenUITest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenLoadingTest() = runComposeUiTest {
        val loadingUIState = Loading
        setContent {
            ExpensesScreen(uiState = loadingUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_SCREEN_LOADING_TEST_TAG).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenErrorTextTest() = runComposeUiTest {
        val errorText = "Error to loading"
        val assertErrorText = "Error: $errorText"
        val errorUIState = Failure(Exception(errorText))
        setContent {
            ExpensesScreen(uiState = errorUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_SCREEN_ERROR_TEST_TAG).assertExists()
        onNodeWithTag(EXPENSE_SCREEN_CONTENT_TEST_TAG).assertTextEquals(assertErrorText)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenErrorTest() = runComposeUiTest {
        val errorUIState = Failure(Exception("Error"))
        setContent {
            ExpensesScreen(uiState = errorUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_SCREEN_ERROR_TEST_TAG).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenSuccessTest() = runComposeUiTest {
        val successUIState = Success<ExpensesUiState>(
            ExpensesUiState(
                expenses = listOf(
                    Expense(
                        id = 1,
                        amount = 200.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test",
                    ),
                    Expense(
                        id = 2,
                        amount = 100.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test"
                    )
                ),
                total = 300.0
            )
        )
        setContent {
            ExpensesScreen(uiState = successUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_SCREEN_SUCCESS_TEST_TAG).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenSuccessEmptyTest() = runComposeUiTest {
        val successUIState = Success<ExpensesUiState>(
            ExpensesUiState(
                expenses = emptyList(),
                total = 0.0
            )
        )
        setContent {
            ExpensesScreen(uiState = successUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_EMPTY_SUCCESS_TEST_TAG).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenSuccessTotalTest() = runComposeUiTest {
        val successUIState = Success<ExpensesUiState>(
            ExpensesUiState(
                expenses = listOf(
                    Expense(
                        id = 1,
                        amount = 200.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test",
                    ),
                    Expense(
                        id = 2,
                        amount = 100.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test"
                    )
                ),
                total = 300.0
            )
        )
        setContent {
            ExpensesScreen(uiState = successUIState, onExpenseAction = {}, onDeleteExpense = {})
        }
        onNodeWithTag(EXPENSE_SCREEN_SUCCESS_TEST_TAG).assertExists()
        onNodeWithTag(EXPENSE_SCREEN_SUCCESS_TOTAL_TEST_TAG).assertTextEquals("$300.0")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expenseScreenSuccessClickItemTest() = runComposeUiTest {
        val successUIState = Success<ExpensesUiState>(
            ExpensesUiState(
                expenses = listOf(
                    Expense(
                        id = 1,
                        amount = 200.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test",
                    ),
                    Expense(
                        id = 2,
                        amount = 100.0,
                        category = ExpenseCategory.GROCERIES,
                        description = "Test"
                    )
                ),
                total = 300.0
            )
        )
        setContent {
            val navigator = rememberNavigator()
            val colors = GetColorsTheme()
            PreComposeApp {
                NavHost(
                    modifier = Modifier.background(colors.bacGroundColor),
                    navigator = navigator,
                    initialRoute = "/home"
                ) {
                    scene(route = "/home") {
                        ExpensesScreen(uiState = successUIState, onExpenseAction = { result ->
                            when (result) {
                                is ExpenseItemActions.Delete -> {}
                                is ExpenseItemActions.Edit -> navigator.navigate("/addExpenses/${result.expense.id}")
                            }
                        }, onDeleteExpense = { })
                    }
                    scene(route = "/addExpenses/{id}?") { _ ->
                        ExpenseDetailScreen(
                            successUIState.data.expenses[0],
                            categoryList = ExpenseManager.getCategories()
                        ) { _ ->

                        }
                    }
                }
            }
        }
        onNodeWithTag(EXPENSE_SCREEN_CLICK_ITEM_TEST_TAG.plus("_1")).performClick()
        waitUntilExactlyOneExists(hasTestTag(EXPENSE_DETAIL_TEST_TAG))
        onNodeWithTag(EXPENSE_DETAIL_TEST_TAG).assertExists()
    }
}