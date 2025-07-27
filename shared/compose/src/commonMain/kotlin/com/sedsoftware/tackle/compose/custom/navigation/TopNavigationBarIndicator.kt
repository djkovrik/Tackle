package com.sedsoftware.tackle.compose.custom.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TopNavigationTab
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.publications_tab_global
import tackle.shared.compose.generated.resources.publications_tab_local

@Composable
internal fun TopNavigationBarIndicator(
    indicatorShape: Shape,
    indicatorColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .background(color = indicatorColor, shape = indicatorShape),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {}
}

@Composable
internal fun Modifier.topNavigationBarIndicatorOffset(
    currentTabPosition: TabPosition,
): Modifier {
    val currentTabWidth: Dp by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    return this then Modifier
        .wrapContentSize(CenterStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

@Preview
@Composable
private fun TopNavigationBarIndicatorPreview() {
    TackleScreenPreview {
        TopNavigationBar(
            selectedTabIndex = 0,
            tabs = listOf(
                TopNavigationTab(
                    textResource = Res.string.publications_tab_local,
                    onClick = {},
                ),
                TopNavigationTab(
                    textResource = Res.string.publications_tab_global,
                    onClick = {},
                ),
            )
        )
    }
}
