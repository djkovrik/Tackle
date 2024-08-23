package com.sedsoftware.tackle.compose.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
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

    Scaffold(
        modifier = modifier.imePadding(),
        bottomBar = {
            BottomNavigationBar(
                activeTab = activeTab,
                modifier = Modifier.fillMaxWidth(),
                onTabClick = component::onTabClicked,
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { scaffoldPadding: PaddingValues ->
        ChildrenContent(
            component = component,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = scaffoldPadding)
                .consumeWindowInsets(paddingValues = scaffoldPadding)
                .systemBarsPadding(),
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
