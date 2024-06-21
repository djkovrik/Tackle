package com.sedsoftware.tackle.compose.widget.navigation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import com.sedsoftware.tackle.main.model.TackleNavigationTab

@Composable
internal fun BottomNavigationBarIndicator(
    indicatorOffset: Dp,
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    itemCount: Int = TackleNavigationTab.entries.size,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(fraction = 1f / itemCount)
            .offset(x = indicatorOffset, y = (-12).dp),
    ) {
        Column(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {
            Indicator(
                indicatorColor = indicatorColor,
                modifier = Modifier.size(size = 8.dp),
            )
        }
    }
}

@Composable
private fun Indicator(
    indicatorColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = indicatorColor, shape = CircleShape)
            .clip(shape = CircleShape)
    )
}

@Composable
@Preview
private fun BottomNavigationBarIndicatorPreview() {
    TackleScreenPreview {
        BottomNavigationBar(
            activeTab = TackleNavigationTab.HOME
        )
    }
}
