import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ExampleTest {

    @Test
    fun myFirstTest() {
        //Given
        val x = 5
        val y = 10
        //When
        val result = x + y
        //Then
        assertNotEquals(10, result)
    }

    @Test
    fun expenseModelListTest() {
        //Given
        val list = mutableListOf<Expense>()
        val expense = Expense(1, 10.0, ExpenseCategory.CAR, "Expense 1")
        //When
        list.add(expense)
        //Then
        assertContains(list, expense)
    }

    @Test
    fun expense_model_param_test() {
        //Given
        val list = mutableListOf<Expense>()
        val expense = Expense(1, 10.0, ExpenseCategory.OTHER, "Expense 1")
        val expense2 = Expense(2, 20.0, ExpenseCategory.OTHER, "Expense 2")
        //When
        list.add(expense)
        list.add(expense2)
        //Then
        assertContains(list, expense)
    }

    @Test
    fun expense_model_param_test_success() {
        //Given
        val list = mutableListOf<Expense>()
        val expense = Expense(1, 10.0, ExpenseCategory.OTHER, "Expense 1")
        val expense2 = Expense(2, 20.0, ExpenseCategory.OTHER, "Expense 2")
        //When
        list.add(expense)
        list.add(expense2)
        //Then
        assertEquals(expense.category, expense2.category)
    }

    @Test
    fun expense_model_param_test_error() {
        //Given
        val list = mutableListOf<Expense>()
        val expense = Expense(1, 10.0, ExpenseCategory.CAR, "Expense 1")
        val expense2 = Expense(2, 20.0, ExpenseCategory.OTHER, "Expense 2")
        //When
        list.add(expense)
        list.add(expense2)
        //Then
        assertNotEquals(expense.category, expense2.category)
    }
}