package com.sedsoftware.tackle.compose.widget.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    showAsButton: Boolean = false,
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
                    showAsButton && activeTab == baseTab -> config.buttonIconColorSelected
                    !showAsButton && activeTab == baseTab -> config.iconColorSelected
                    showAsButton && activeTab != baseTab -> config.buttonIconColorNormal
                    else -> config.iconColorNormal
                }
            )
            val animatedBackgroundColor: Color by animateColorAsState(
                if (activeTab == baseTab) {
                    config.buttonBackgroundColorSelected
                } else {
                    config.buttonBackgroundColorNormal
                }
            )

            if (showAsButton) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .background(color = animatedBackgroundColor, shape = CircleShape)
                        .size(size = animatedIconSize + 8.dp)
                ) {
                    Icon(
                        painter = painterResource(resource = iconRes),
                        contentDescription = stringResource(resource = contentDescriptionRes),
                        tint = animatedIconColor,
                        modifier = modifier.size(size = animatedIconSize),
                    )
                }
            } else {
                Icon(
                    painter = painterResource(resource = iconRes),
                    contentDescription = stringResource(resource = contentDescriptionRes),
                    tint = animatedIconColor,
                    modifier = modifier.size(size = animatedIconSize),
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = config.containerColor),
        selected = activeTab == baseTab,
        onClick = { onTabClick.invoke(baseTab) },
        alwaysShowLabel = false,
    )
}
