package com.sedsoftware.tackle.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    override val childStack: Value<ChildStack<*, Child>>
        get() = TODO("Not yet implemented")

    @Serializable
    private sealed interface Config {

    }
}
