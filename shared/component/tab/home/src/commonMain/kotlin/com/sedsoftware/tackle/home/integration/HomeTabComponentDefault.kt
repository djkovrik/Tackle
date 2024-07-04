package com.sedsoftware.tackle.home.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.HomeTabComponent.Model

@Suppress("UnusedPrivateProperty")
class HomeTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (ComponentOutput) -> Unit,
) : HomeTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Home tab component")
    )
}
