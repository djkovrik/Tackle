package com.sedsoftware.tackle.compose.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.custom.navigation.BottomNavigationBar
import com.sedsoftware.tackle.compose.ui.explore.ExploreTabContent
import com.sedsoftware.tackle.compose.ui.home.HomeTabContent
import com.sedsoftware.tackle.compose.ui.notifications.NotificationsTabContent
import com.sedsoftware.tackle.compose.ui.profile.ProfileTabContent
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
        is MainComponent.Child.HomeTab -> TackleNavigationTab.HOME
        is MainComponent.Child.ExploreTab -> TackleNavigationTab.EXPLORE
        is MainComponent.Child.PublicationsTab -> TackleNavigationTab.PUBLICATIONS
        is MainComponent.Child.NotificationsTab -> TackleNavigationTab.NOTIFICATIONS
        is MainComponent.Child.ProfileTab -> TackleNavigationTab.PROFILE
    }

    Column(modifier = modifier) {
        ChildrenContent(
            component = component,
            modifier = Modifier.weight(weight = 1f)
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
        animation = stackAnimation(animator = fade()),
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is MainComponent.Child.HomeTab -> HomeTabContent(component = child.component)
            is MainComponent.Child.ExploreTab -> ExploreTabContent(component = child.component)
            is MainComponent.Child.PublicationsTab -> PublicationsTabContent(component = child.component)
            is MainComponent.Child.NotificationsTab -> NotificationsTabContent(component = child.component)
            is MainComponent.Child.ProfileTab -> ProfileTabContent(component = child.component)
        }
    }
}
