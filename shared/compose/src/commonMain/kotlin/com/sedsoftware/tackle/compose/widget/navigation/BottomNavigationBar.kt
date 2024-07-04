package com.sedsoftware.tackle.compose.widget.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.NavigationBarConfig
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.main.model.TackleNavigationTab
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
        containerColor = MaterialTheme.colorScheme.background,
        iconColorNormal = MaterialTheme.colorScheme.secondary,
        iconColorSelected = MaterialTheme.colorScheme.primary,
        buttonIconColorNormal = MaterialTheme.colorScheme.inverseOnSurface,
        buttonIconColorSelected = MaterialTheme.colorScheme.inverseOnSurface,
        buttonBackgroundColorNormal = MaterialTheme.colorScheme.secondary,
        buttonBackgroundColorSelected = MaterialTheme.colorScheme.primary,
        iconSizeNormal = 34.dp,
        iconSizeSelected = 38.dp,
    ),
    itemsCount: Int = TackleNavigationTab.entries.size,
    onTabClick: (TackleNavigationTab) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth: Dp = this.maxWidth

        val animatedIndicatorOffset: Dp by animateDpAsState(
            targetValue = (maxWidth / itemsCount) * activeTab.index + (-3.5 + activeTab.index * 1.5).dp,
            animationSpec = spring(
                dampingRatio = 1f,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )

        Column(
            modifier = modifier
                .background(color = config.containerColor)
                .navigationBarsPadding()
        ) {
            NavigationBar(
                containerColor = config.containerColor,
                contentColor = config.containerColor,
                tonalElevation = 0.dp,
                modifier = modifier,
            ) {
                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.HOME,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_home,
                    contentDescriptionRes = Res.string.main_tab_home,
                    onTabClick = onTabClick,
                )

                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.EXPLORE,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_explore,
                    contentDescriptionRes = Res.string.main_tab_explore,
                    onTabClick = onTabClick,
                )

                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.EDITOR,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_editor,
                    contentDescriptionRes = Res.string.main_tab_editor,
                    showAsButton = true,
                    onTabClick = onTabClick,
                )

                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.PUBLICATIONS,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_publications,
                    contentDescriptionRes = Res.string.main_tab_publications,
                    onTabClick = onTabClick,
                )

                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.NOTIFICATIONS,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_notifications,
                    contentDescriptionRes = Res.string.main_tab_notifications,
                    onTabClick = onTabClick,
                )
            }

            BottomNavigationBarIndicator(
                indicatorOffset = animatedIndicatorOffset,
                indicatorColor = config.iconColorSelected,
            )
        }
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
