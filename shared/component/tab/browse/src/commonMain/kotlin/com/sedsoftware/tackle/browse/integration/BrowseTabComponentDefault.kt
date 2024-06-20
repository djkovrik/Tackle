package com.sedsoftware.tackle.browse.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.browse.BrowseTabComponent
import com.sedsoftware.tackle.browse.BrowseTabComponent.Model

class BrowseTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (BrowseTabComponent.Output) -> Unit,
) : BrowseTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Browse tab component")
    )
}
