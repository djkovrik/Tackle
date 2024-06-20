package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun BottomNavigationBarIndicator(
    indicatorOffset: Dp,
    indicatorColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .offset(x = indicatorOffset)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(color = Color.Green)
                .align(alignment = Alignment.BottomCenter)
        ) {
            Indicator(indicatorColor = indicatorColor)
            Spacer(modifier = Modifier.height(height = 2.dp))
        }
    }
}

@Composable
private fun Indicator(indicatorColor: Color) {
    Box(
        modifier = Modifier
            .size(size = 8.dp)
            .background(color = indicatorColor, shape = CircleShape)
            .clip(shape = CircleShape)
    )
}

@Composable
@Preview
private fun BottomNavigationBarIndicatorPreview() {
    TackleScreenPreview {
        BottomNavigationBarIndicator(
            indicatorOffset = 0.dp,
            indicatorColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(120.dp)
        )
    }
}
