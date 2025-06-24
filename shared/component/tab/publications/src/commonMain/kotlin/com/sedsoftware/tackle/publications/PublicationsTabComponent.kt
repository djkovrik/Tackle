package com.sedsoftware.tackle.publications

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.statuslist.StatusListComponent

interface PublicationsTabComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onLocalTabClick()
    fun onRemoteTabClick()

    sealed class Child {
        class LocalTimeline(val component: StatusListComponent) : Child()
        class RemoteTimeline(val component: StatusListComponent) : Child()
    }
}
