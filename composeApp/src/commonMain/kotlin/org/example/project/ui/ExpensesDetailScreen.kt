package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.GetColorsTheme
import org.example.project.data.TitleTopBarTypes
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import org.example.project.utils.EXPENSE_DETAIL_TEST_TAG

@Composable
fun ExpenseDetailScreen(
    expenseToEdit: Expense? = null,
    categoryList: List<ExpenseCategory> = emptyList(),
    addExpenseNavigateBack: (Expense) -> Unit
) {

    val colors = GetColorsTheme()
    var price by rememberSaveable { mutableStateOf(expenseToEdit?.amount ?: 0.0) }
    var description by rememberSaveable { mutableStateOf(expenseToEdit?.description ?: "") }
    var category by rememberSaveable { mutableStateOf(expenseToEdit?.category?.name ?: "") }
    var categorySelected by rememberSaveable {
        mutableStateOf(
            expenseToEdit?.category?.name ?: "Select Category"
        )
    }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == ModalBottomSheetValue.Expanded) {
            keyboardController?.hide()
        }
    }

    ModalBottomSheetLayout(sheetState = sheetState, sheetContent = {
        CategoryBottomSheetContent(categoryList) {
            category = it.name
            categorySelected = it.name
            scope.launch {
                sheetState.hide()
            }
        }
    }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
                .testTag(EXPENSE_DETAIL_TEST_TAG)
        ) {
            ExpenseAmount(price, keyboardController) {
                price = it
            }
            Spacer(modifier = Modifier.height(30.dp))
            ExpenseTypeSelector(categorySelected) {
                scope.launch {
                    sheetState.show()
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            ExpenseDescription(description, keyboardController) {
                description = it
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(45)),
                onClick = {
                    val expense = Expense(
                        amount = price,
                        description = description,
                        category = ExpenseCategory.valueOf(category)
                    )
                    val expenseFromEdit = expenseToEdit?.let {
                        expense.copy(id = it.id)
                    }
                    addExpenseNavigateBack(expenseFromEdit ?: expense)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.purple,
                    contentColor = Color.White
                ), enabled = price != 0.0 && category.isNotBlank() && description.isNotBlank()
            ) {
                expenseToEdit?.let {
                    Text(TitleTopBarTypes.EDIT_EXPENSE.value)
                } ?: run {
                    Text(TitleTopBarTypes.ADD_EXPENSE.value)
                }
            }
        }
    }
}

@Composable
fun ExpenseAmount(
    priceContent: Double,
    keyboardController: SoftwareKeyboardController?,
    onPriceChange: (Double) -> Unit
) {
    val colors = GetColorsTheme()
    var text by rememberSaveable { mutableStateOf("$priceContent") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Amount",
            color = Color.Gray,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textColor
            )
            TextField(
                value = text, modifier = Modifier.weight(1f), onValueChange = { newText ->
                    val numericText = newText.filter { it.isDigit() || it == '.' }
                    text = if (numericText.isNotEmpty() && numericText.count { it == '.' } <= 1) {
                        try {
                            val newValue = numericText.toDouble()
                            onPriceChange(newValue)
                            numericText
                        } catch (_: NumberFormatException) {
                            ""
                        }
                    } else {
                        onPriceChange(0.0)
                        ""
                    }
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ), keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }), singleLine = true, colors = TextFieldDefaults.textFieldColors(
                    textColor = colors.textColor,
                    backgroundColor = colors.bacGroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent
                ), textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
            )
            Text("USD", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
        }
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Composable
private fun ExpenseTypeSelector(categorySelected: String, openBottomSheet: () -> Unit) {
    val colors = GetColorsTheme()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Expenses made for",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Text(
                text = categorySelected,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor
            )
        }
        IconButton(
            modifier = Modifier.clip(RoundedCornerShape(30)).background(colors.colorArrowRound),
            onClick = { openBottomSheet() }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = colors.textColor
            )
        }
    }
}

@Composable
fun ExpenseDescription(
    descriptionContent: String,
    keyboardController: SoftwareKeyboardController?,
    onDescriptionChange: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf(descriptionContent) }
    val colors = GetColorsTheme()
    Column {
        Text("Description", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newText ->
                if (newText.length <= 200) {
                    text = newText
                    onDescriptionChange(text)
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = colors.textColor,
                backgroundColor = colors.bacGroundColor,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Composable
private fun CategoryBottomSheetContent(
    categories: List<ExpenseCategory>,
    onCategorySelected: (ExpenseCategory) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(categories) { category ->
            CategoryItem(category, onCategorySelected)
        }
    }
}

@Composable
private fun CategoryItem(category: ExpenseCategory, onCategorySelected: (ExpenseCategory) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
            onCategorySelected(category)
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            imageVector = category.icon,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Text(text = category.name)
    }
}