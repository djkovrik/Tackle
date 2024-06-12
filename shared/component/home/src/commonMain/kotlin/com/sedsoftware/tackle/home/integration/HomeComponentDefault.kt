package com.sedsoftware.tackle.home.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.home.HomeComponent
import com.sedsoftware.tackle.home.HomeComponent.Model

@Suppress("UnusedPrivateProperty")
class HomeComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (HomeComponent.Output) -> Unit,
) : HomeComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Home component")
    )
}
