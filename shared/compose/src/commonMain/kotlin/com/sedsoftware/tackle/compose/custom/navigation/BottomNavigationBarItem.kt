package com.sedsoftware.tackle.compose.custom.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.sedsoftware.tackle.compose.model.NavigationBarConfig
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RowScope.BottomNavigationBarItem(
    baseTab: TackleNavigationTab,
    activeTab: TackleNavigationTab,
    config: NavigationBarConfig,
    iconRes: DrawableResource,
    contentDescriptionRes: StringResource,
    modifier: Modifier = Modifier,
    onTabClick: (TackleNavigationTab) -> Unit = {},
) {
    NavigationBarItem(
        icon = {
            val animatedIconSize: Dp by animateDpAsState(
                if (activeTab == baseTab) {
                    config.iconSizeSelected
                } else {
                    config.iconSizeNormal
                },
            )
            val animatedIconColor: Color by animateColorAsState(
                when {
                    activeTab == baseTab -> config.iconColorSelected
                    else -> config.iconColorNormal
                }
            )

            Icon(
                painter = painterResource(resource = iconRes),
                contentDescription = stringResource(resource = contentDescriptionRes),
                tint = animatedIconColor,
                modifier = modifier.size(size = animatedIconSize),
            )
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = config.containerColor),
        selected = activeTab == baseTab,
        onClick = { onTabClick.invoke(baseTab) },
        alwaysShowLabel = false,
    )
}
