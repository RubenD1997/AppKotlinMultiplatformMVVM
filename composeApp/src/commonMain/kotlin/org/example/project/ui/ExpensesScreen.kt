package org.example.project.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.GetColorsTheme
import org.example.project.core.Resource
import org.example.project.model.Expense
import org.example.project.presentation.ExpensesUiState
import org.example.project.ui.ExpenseItemActions.Delete
import org.example.project.ui.ExpenseItemActions.Edit
import org.example.project.utils.EXPENSE_EMPTY_SUCCESS_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_CLICK_ITEM_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_CONTENT_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_ERROR_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_LOADING_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_SUCCESS_TEST_TAG
import org.example.project.utils.EXPENSE_SCREEN_SUCCESS_TOTAL_TEST_TAG
import org.example.project.utils.SwipeToDeleteContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    uiState: Resource<ExpensesUiState>,
    onExpenseAction: (ExpenseItemActions) -> Unit,
    onDeleteExpense: (Expense) -> Unit
) {
    val colors = GetColorsTheme()
    when (val result = uiState) {
        is Resource.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize().testTag(EXPENSE_SCREEN_ERROR_TEST_TAG),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.exception.message.toString(),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.testTag(EXPENSE_SCREEN_CONTENT_TEST_TAG)
                )
            }
        }

        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().testTag(EXPENSE_SCREEN_LOADING_TEST_TAG),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            if (result.data.expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                        .testTag(EXPENSE_EMPTY_SUCCESS_TEST_TAG),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No expenses found, please add your first expense",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        .testTag(EXPENSE_SCREEN_SUCCESS_TEST_TAG),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickyHeader {
                        Column(modifier = Modifier.background(colors.bacGroundColor)) {
                            ExpensesTotalHeader(result.data.total)
                            AllExpensesHeader()
                        }
                    }
                    items(result.data.expenses, key = { it.id }) { item ->
                        SwipeToDeleteContainer(item = item, onDelete = { onDeleteExpense(it) }) {
                            ExpensesItem(expense = item) {
                                onExpenseAction(it)
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ExpensesTotalHeader(total: Double) {
    Card(shape = RoundedCornerShape(30), backgroundColor = Color.Black, elevation = 5.dp) {
        Box(
            modifier = Modifier.fillMaxWidth().height(130.dp).padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "$$total",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.testTag(EXPENSE_SCREEN_SUCCESS_TOTAL_TEST_TAG)
            )
            Text("USD", modifier = Modifier.align(Alignment.CenterEnd), color = Color.Gray)
        }
    }
}

@Composable
fun AllExpensesHeader() {
    val colors = GetColorsTheme()
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "All Expenses",
            modifier = Modifier.weight(1f),
            color = colors.textColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray), enabled = false
        ) {
            Text("View All", modifier = Modifier)
        }
    }
}

sealed class ExpenseItemActions {
    data class Delete(val expense: Expense) : ExpenseItemActions()
    data class Edit(val expense: Expense) : ExpenseItemActions()
}

@Composable
fun ExpensesItem(expense: Expense, onExpenseAction: (ExpenseItemActions) -> Unit) {
    val color = GetColorsTheme()
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
            .clickable { onExpenseAction(Edit(expense)) }
            .testTag(EXPENSE_SCREEN_CLICK_ITEM_TEST_TAG.plus("_${expense.id}")),
        backgroundColor = color.colorExpenseItem,
        shape = RoundedCornerShape(30)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(35),
                color = color.purple
            ) {
                Image(
                    modifier = Modifier.padding(10.dp),
                    imageVector = expense.icon,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    expense.category.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = color.textColor
                )
                Text(
                    expense.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
            Text(
                "$${expense.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = color.textColor
            )
        }
    }
}