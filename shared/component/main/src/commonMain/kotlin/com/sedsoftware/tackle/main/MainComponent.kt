package com.sedsoftware.tackle.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.browse.BrowseTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.feeds.FeedsTabComponent
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.notifications.NotificationsTabComponent

interface MainComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onHomeTabClicked()
    fun onBrowseTabClicked()
    fun onEditorTabClicked()
    fun onFeedsTabClicked()
    fun onNotificationsTabClicked()

    sealed class Child {
        class TabHome(val component: HomeTabComponent) : Child()
        class TabBrowse(val component: BrowseTabComponent) : Child()
        class TabEditor(val component: EditorTabComponent) : Child()
        class TabFeeds(val component: FeedsTabComponent) : Child()
        class TabNotifications(val component: NotificationsTabComponent) : Child()
    }

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
