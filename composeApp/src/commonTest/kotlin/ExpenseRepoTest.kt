import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepositoryImpl
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ExpenseRepoTest {
    private val expenseManger = ExpenseManager
    private val repo = ExpensesRepositoryImpl(expenseManger)

    @Test
    fun expense_list_is_not_empty() {
        //Give
        val expenseList = mutableListOf<Expense>()
        //When
        expenseList.addAll(repo.getExpenses())
        //Then
        assertTrue(expenseList.isNotEmpty())
    }

    @Test
    fun add_new_expense() {
        //Give
        val expenseList = repo.getExpenses()
        //When
        repo.addExpense(
            Expense(
                amount = 10.0,
                category = ExpenseCategory.OTHER,
                description = "Expense 1"
            )
        )
        //Then
        assertContains(expenseList, expenseList.find { it.id == 7L })
    }

    @Test
    fun edit_expense() {
        //Give
        val expenseListBefore = repo.getExpenses()
        //When
        val newExpenseId = 7L
        repo.addExpense(
            Expense(
                amount = 10.0,
                category = ExpenseCategory.OTHER,
                description = "Expense 1"
            )
        )
        assertNotNull(expenseListBefore.find { it.id == newExpenseId })

        val updatedExpense = Expense(
            id = newExpenseId,
            amount = 10.0,
            category = ExpenseCategory.OTHER,
            description = "Clothes"
        )

        repo.editExpense(updatedExpense)

        //Then
        val expenseListAfter = repo.getExpenses()
        assertEquals(updatedExpense, expenseListAfter.find { it.id == newExpenseId })
    }

    @Test
    fun getAllCategories() {
        //Give
        val categoryList = mutableListOf<ExpenseCategory>()
        //When
        categoryList.addAll(repo.getCategories())
        //Then
        assertTrue(categoryList.isNotEmpty())
    }

    @Test
    fun check_all_categories() {
        //Give
        val allCategories = ExpenseCategory.entries
        //When
        val categoryList = repo.getCategories()
        //Then
        assertEquals(allCategories, categoryList)
    }
}