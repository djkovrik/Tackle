package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
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
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.main_tab_editor
import tackle.shared.compose.generated.resources.main_tab_explore
import tackle.shared.compose.generated.resources.main_tab_home
import tackle.shared.compose.generated.resources.main_tab_notifications
import tackle.shared.compose.generated.resources.main_tab_publications
import tackle.shared.compose.generated.resources.tab_editor
import tackle.shared.compose.generated.resources.tab_explore
import tackle.shared.compose.generated.resources.tab_home
import tackle.shared.compose.generated.resources.tab_notifications
import tackle.shared.compose.generated.resources.tab_publications

@Composable
internal fun BottomNavigationBar(
    activeTab: TackleNavigationTab,
    modifier: Modifier = Modifier,
    config: NavigationBarConfig = NavigationBarConfig(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        iconColorNormal = MaterialTheme.colorScheme.onPrimaryContainer,
        iconColorSelected = MaterialTheme.colorScheme.primary,
        editorIconColorNormal = MaterialTheme.colorScheme.inversePrimary,
        editorIconColorSelected = MaterialTheme.colorScheme.inversePrimary,
        buttonColor = MaterialTheme.colorScheme.primary,
        iconSizeNormal = 32.dp,
        iconSizeSelected = 38.dp,
        editorBackgroundSizeNormal = 44.dp,
        editorBackgroundSizeSelected = 50.dp,
    ),
    onTabClick: (TackleNavigationTab) -> Unit = {},
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.navigationBarsPadding(),
    ) {
        NavigationBarItem(
            icon = {
                val animatedIconSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.HOME) {
                        config.iconSizeSelected
                    } else {
                        config.iconSizeNormal
                    },
                )
                val animatedIconColor: Color by animateColorAsState(
                    if (activeTab == TackleNavigationTab.HOME) {
                        config.iconColorSelected
                    } else {
                        config.iconColorNormal
                    }
                )

                Icon(
                    painter = painterResource(Res.drawable.tab_home),
                    contentDescription = stringResource(Res.string.main_tab_home),
                    tint = animatedIconColor,
                    modifier = Modifier.size(size = animatedIconSize),
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = config.indicatorColor),
            selected = activeTab == TackleNavigationTab.HOME,
            onClick = { onTabClick.invoke(TackleNavigationTab.HOME) },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                val animatedIconSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.EXPLORE) {
                        config.iconSizeSelected
                    } else {
                        config.iconSizeNormal
                    },
                )
                val animatedIconColor: Color by animateColorAsState(
                    if (activeTab == TackleNavigationTab.EXPLORE) {
                        config.iconColorSelected
                    } else {
                        config.iconColorNormal
                    }
                )

                Icon(
                    painter = painterResource(Res.drawable.tab_explore),
                    contentDescription = stringResource(Res.string.main_tab_explore),
                    tint = animatedIconColor,
                    modifier = Modifier.size(size = animatedIconSize),
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = config.indicatorColor),
            selected = activeTab == TackleNavigationTab.EXPLORE,
            onClick = { onTabClick.invoke(TackleNavigationTab.EXPLORE) },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                val animatedIconSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.EDITOR) {
                        config.iconSizeSelected
                    } else {
                        config.iconSizeNormal
                    },
                )
                val animatedIconColor: Color by animateColorAsState(
                    if (activeTab == TackleNavigationTab.EDITOR) {
                        config.iconColorSelected
                    } else {
                        config.iconColorNormal
                    }
                )
                val animatedBackgroundSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.EDITOR) {
                        config.editorBackgroundSizeSelected
                    } else {
                        config.editorBackgroundSizeNormal
                    },
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(color = config.buttonColor, shape = CircleShape)
                        .size(size = animatedBackgroundSize)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tab_editor),
                        contentDescription = stringResource(Res.string.main_tab_editor),
                        tint = animatedIconColor,
                        modifier = Modifier.size(size = animatedIconSize),
                    )
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = config.indicatorColor),
            selected = activeTab == TackleNavigationTab.EDITOR,
            onClick = { onTabClick.invoke(TackleNavigationTab.EDITOR) },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                val animatedIconSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.PUBLICATIONS) {
                        config.iconSizeSelected
                    } else {
                        config.iconSizeNormal
                    },
                )
                val animatedIconColor: Color by animateColorAsState(
                    if (activeTab == TackleNavigationTab.PUBLICATIONS) {
                        config.iconColorSelected
                    } else {
                        config.iconColorNormal
                    }
                )

                Icon(
                    painter = painterResource(Res.drawable.tab_publications),
                    contentDescription = stringResource(Res.string.main_tab_publications),
                    tint = animatedIconColor,
                    modifier = Modifier.size(size = animatedIconSize),
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = config.indicatorColor),
            selected = activeTab == TackleNavigationTab.PUBLICATIONS,
            onClick = { onTabClick.invoke(TackleNavigationTab.PUBLICATIONS) },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                val animatedIconSize: Dp by animateDpAsState(
                    if (activeTab == TackleNavigationTab.NOTIFICATIONS) {
                        config.iconSizeSelected
                    } else {
                        config.iconSizeNormal
                    },
                )
                val animatedIconColor: Color by animateColorAsState(
                    if (activeTab == TackleNavigationTab.NOTIFICATIONS) {
                        config.iconColorSelected
                    } else {
                        config.iconColorNormal
                    }
                )

                Icon(
                    painter = painterResource(Res.drawable.tab_notifications),
                    contentDescription = stringResource(Res.string.main_tab_notifications),
                    tint = animatedIconColor,
                    modifier = Modifier.size(size = animatedIconSize),
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = config.indicatorColor),
            selected = activeTab == TackleNavigationTab.NOTIFICATIONS,
            onClick = { onTabClick.invoke(TackleNavigationTab.NOTIFICATIONS) },
            alwaysShowLabel = false,
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarHomePreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            activeTab = TackleNavigationTab.HOME
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarHomePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            activeTab = TackleNavigationTab.HOME
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarExplorePreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            activeTab = TackleNavigationTab.EXPLORE
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarExplorePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(activeTab = TackleNavigationTab.EXPLORE)
    }
}

@Composable
@Preview
private fun BottomNavigationBarEditorPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(activeTab = TackleNavigationTab.EDITOR)
    }
}

@Composable
@Preview
private fun BottomNavigationBarEditorPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(activeTab = TackleNavigationTab.EDITOR)
    }
}

@Composable
@Preview
private fun BottomNavigationBarPublicationsPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(activeTab = TackleNavigationTab.PUBLICATIONS)
    }
}

@Composable
@Preview
private fun BottomNavigationBarPublicationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(activeTab = TackleNavigationTab.PUBLICATIONS)
    }
}

@Composable
@Preview
private fun BottomNavigationBarNotificationsPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(activeTab = TackleNavigationTab.NOTIFICATIONS)
    }
}

@Composable
@Preview
private fun BottomNavigationBarNotificationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(activeTab = TackleNavigationTab.NOTIFICATIONS)
    }
}
