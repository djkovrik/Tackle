package com.sedsoftware.tackle.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.profile.ProfileTabComponent
import com.sedsoftware.tackle.publications.PublicationsTabComponent

interface MainComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: TackleNavigationTab)

    sealed class Child {
        class HomeTab(val component: HomeTabComponent) : Child()
        class ExploreTab(val component: ExploreTabComponent) : Child()
        class PublicationsTab(val component: PublicationsTabComponent) : Child()
        class NotificationsTab(val component: NotificationsTabComponent) : Child()
        class ProfileTab(val component: ProfileTabComponent) : Child()
    }
}
