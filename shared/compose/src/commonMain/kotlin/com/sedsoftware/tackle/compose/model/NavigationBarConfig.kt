package com.sedsoftware.tackle.compose.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class NavigationBarConfig(
    val containerColor: Color,
    val indicatorColor: Color,
    val iconColorNormal: Color,
    val iconColorSelected: Color,
    val editorIconColorNormal: Color,
    val editorIconColorSelected: Color,
    val buttonColor: Color,
    val iconSizeNormal: Dp,
    val iconSizeSelected: Dp,
    val editorBackgroundSizeNormal: Dp,
    val editorBackgroundSizeSelected: Dp,
)
