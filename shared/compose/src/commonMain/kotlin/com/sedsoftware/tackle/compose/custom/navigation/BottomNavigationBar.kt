package com.sedsoftware.tackle.compose.custom.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.main_tab_explore
import tackle.shared.compose.generated.resources.main_tab_home
import tackle.shared.compose.generated.resources.main_tab_notifications
import tackle.shared.compose.generated.resources.main_tab_profile
import tackle.shared.compose.generated.resources.main_tab_publications
import tackle.shared.compose.generated.resources.tab_explore
import tackle.shared.compose.generated.resources.tab_home
import tackle.shared.compose.generated.resources.tab_notifications
import tackle.shared.compose.generated.resources.tab_profile
import tackle.shared.compose.generated.resources.tab_publications

@Composable
internal fun BottomNavigationBar(
    activeTab: TackleNavigationTab,
    modifier: Modifier = Modifier,
    config: NavigationBarConfig = NavigationBarConfig(
        containerColor = MaterialTheme.colorScheme.background,
        iconColorNormal = MaterialTheme.colorScheme.secondary,
        iconColorSelected = MaterialTheme.colorScheme.primary,
        iconSizeNormal = 32.dp,
        iconSizeSelected = 36.dp,
    ),
    itemsCount: Int = TackleNavigationTab.entries.size,
    onTabClick: (TackleNavigationTab) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth: Dp = this.maxWidth

        val animatedIndicatorOffset: Dp by animateDpAsState(
            targetValue = (maxWidth / itemsCount) * activeTab.index,
            animationSpec = spring(
                dampingRatio = 1f,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )

        Column(modifier = modifier.navigationBarsPadding()) {
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

                BottomNavigationBarItem(
                    baseTab = TackleNavigationTab.PROFILE,
                    activeTab = activeTab,
                    config = config,
                    iconRes = Res.drawable.tab_profile,
                    contentDescriptionRes = Res.string.main_tab_profile,
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

@Composable
@Preview
private fun BottomNavigationBarProfilePreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(activeTab = TackleNavigationTab.PROFILE)
    }
}

@Composable
@Preview
private fun BottomNavigationBarProfilePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(activeTab = TackleNavigationTab.PROFILE)
    }
}
