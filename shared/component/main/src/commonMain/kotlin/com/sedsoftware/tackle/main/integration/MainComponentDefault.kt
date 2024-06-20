package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Model

@Suppress("UnusedPrivateProperty")
class MainComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (MainComponent.Output) -> Unit,
) : MainComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Main component")
    )
}
