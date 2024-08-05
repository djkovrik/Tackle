package com.sedsoftware.tackle.compose.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.custom.navigation.BottomNavigationBar
import com.sedsoftware.tackle.compose.ui.editor.EditorTabContent
import com.sedsoftware.tackle.compose.ui.explore.ExploreTabContent
import com.sedsoftware.tackle.compose.ui.home.HomeTabContent
import com.sedsoftware.tackle.compose.ui.notifications.NotificationsTabContent
import com.sedsoftware.tackle.compose.ui.publications.PublicationsTabContent
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab

@Composable
internal fun MainContent(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.childStack.subscribeAsState()
    val activeComponent: MainComponent.Child = stack.active.instance
    val activeTab: TackleNavigationTab = when (activeComponent) {
        is MainComponent.Child.TabHome -> TackleNavigationTab.HOME
        is MainComponent.Child.TabExplore -> TackleNavigationTab.EXPLORE
        is MainComponent.Child.TabEditor -> TackleNavigationTab.EDITOR
        is MainComponent.Child.TabPublications -> TackleNavigationTab.PUBLICATIONS
        is MainComponent.Child.TabNotifications -> TackleNavigationTab.NOTIFICATIONS
    }

    Column(modifier = modifier) {
        ChildrenContent(
            component = component,
            modifier = Modifier
                .weight(weight = 1f)
                .consumeWindowInsets(insets = WindowInsets.navigationBars)
        )

        BottomNavigationBar(
            activeTab = activeTab,
            modifier = Modifier.fillMaxWidth(),
            onTabClick = component::onTabClicked,
        )
    }
}

@Composable
private fun ChildrenContent(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is MainComponent.Child.TabHome -> HomeTabContent(component = child.component)
            is MainComponent.Child.TabExplore -> ExploreTabContent(component = child.component)
            is MainComponent.Child.TabEditor -> EditorTabContent(component = child.component)
            is MainComponent.Child.TabPublications -> PublicationsTabContent(component = child.component)
            is MainComponent.Child.TabNotifications -> NotificationsTabContent(component = child.component)
        }
    }
}
