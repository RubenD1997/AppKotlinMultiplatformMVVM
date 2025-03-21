package org.example.project

import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.data.SessionCache

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.Black),
        shapes = MaterialTheme.shapes.copy(
            small = AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp)
        )
    ) {
        content()
    }
}


@Composable
fun GetColorsTheme(): DarkModeColors {
    val isDarkModel = SessionCache.isDarkMode()
    val purple = Color(0xFF6A66FF)
    val colorExpenseItem = if (isDarkModel) Color(0xFF090808) else Color(0xFFF1F1F1)
    val bacGroundColor = if (isDarkModel) Color(0xFF1E1C1C) else Color.White
    val textColor = if (isDarkModel) Color.White else Color.Black
    val addIconColor = if (isDarkModel) purple else Color.Black
    val colorArrowRound = if (isDarkModel) purple else Color.Gray.copy(alpha = .2f)
    return DarkModeColors(
        purple = purple,
        colorExpenseItem = colorExpenseItem,
        bacGroundColor = bacGroundColor,
        textColor = textColor,
        addIconColor = addIconColor,
        colorArrowRound = colorArrowRound
    )
}

data class DarkModeColors(
    val purple: Color,
    val colorExpenseItem: Color,
    val bacGroundColor: Color,
    val textColor: Color,
    val addIconColor: Color,
    val colorArrowRound: Color
)