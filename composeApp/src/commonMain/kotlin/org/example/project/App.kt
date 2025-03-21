package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.example.project.data.TitleTopBarTypes
import moe.tlaster.precompose.navigation.path
import org.example.navigation.Navigation
import org.example.project.data.CrossConfigDevice
import org.example.project.data.SessionCache
import org.koin.compose.KoinContext

@Composable
@Preview
fun App(configDevice: CrossConfigDevice? = null) {
    PreComposeApp {
        KoinContext {
            val colors = GetColorsTheme()
            SessionCache.configDevice = configDevice
            AppTheme {
                val navigator = rememberNavigator()
                var titleTopBar = GetTitleTopBar(navigator)
                val isEditOrAddExpense = titleTopBar != TitleTopBarTypes.DASHBOARD.value
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(elevation = 0.dp, title = {
                        Text(text = titleTopBar, fontSize = 25.sp, color = colors.textColor)
                    }, backgroundColor = colors.bacGroundColor, navigationIcon = {
                        if (isEditOrAddExpense) {
                            IconButton(onClick = { navigator.popBackStack() }) {
                                Icon(
                                    modifier = Modifier.padding(start = 16.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "",
                                    tint = colors.textColor
                                )
                            }
                        } else {
                            Icon(
                                modifier = Modifier.padding(start = 16.dp),
                                imageVector = Icons.Default.Apps,
                                contentDescription = "",
                                tint = colors.textColor
                            )
                        }
                    })
                }, floatingActionButton = {
                    if (!isEditOrAddExpense) {
                        FloatingActionButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                navigator.navigate("/addExpenses")
                            },
                            shape = RoundedCornerShape(50),
                            backgroundColor = colors.addIconColor,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }) {
                    Navigation(navigator)
                }
            }
        }
    }
}

@Composable
fun GetTitleTopBar(navigator: Navigator): String {
    var titleTopBar = TitleTopBarTypes.DASHBOARD
    val isOnAddExpenses =
        navigator.currentEntry.collectAsState(null).value?.route?.route.equals("/addExpenses/{id}?")
    if (isOnAddExpenses) {
        titleTopBar = TitleTopBarTypes.ADD_EXPENSE
    }
    val isOnEditExpenses =
        navigator.currentEntry.collectAsState(null).value?.path<Long>("id")
    isOnEditExpenses?.let {
        titleTopBar = TitleTopBarTypes.EDIT_EXPENSE
    }
    return titleTopBar.value
}