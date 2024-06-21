package com.sedsoftware.tackle.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.publications.PublicationsTabComponent

interface MainComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: TackleNavigationTab)

    sealed class Child {
        class TabHome(val component: HomeTabComponent) : Child()
        class TabExplore(val component: ExploreTabComponent) : Child()
        class TabEditor(val component: EditorTabComponent) : Child()
        class TabPublications(val component: PublicationsTabComponent) : Child()
        class TabNotifications(val component: NotificationsTabComponent) : Child()
    }

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
