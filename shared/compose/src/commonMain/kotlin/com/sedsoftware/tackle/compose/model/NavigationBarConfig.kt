package com.sedsoftware.tackle.compose.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

internal data class NavigationBarConfig(
    val containerColor: Color,
    val iconColorNormal: Color,
    val iconColorSelected: Color,
    val buttonIconColorNormal: Color,
    val buttonIconColorSelected: Color,
    val buttonBackgroundColorNormal: Color,
    val buttonBackgroundColorSelected: Color,
    val iconSizeNormal: Dp,
    val iconSizeSelected: Dp,
)
