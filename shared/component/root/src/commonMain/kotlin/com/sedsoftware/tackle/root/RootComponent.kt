package com.sedsoftware.tackle.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.auth.AuthComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Auth(val component: AuthComponent) : Child()
    }
}
