package com.sedsoftware.tackle.feeds.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.feeds.FeedsTabComponent
import com.sedsoftware.tackle.feeds.FeedsTabComponent.Model

class FeedsTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (FeedsTabComponent.Output) -> Unit,
) : FeedsTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Feeds tab component")
    )
}
