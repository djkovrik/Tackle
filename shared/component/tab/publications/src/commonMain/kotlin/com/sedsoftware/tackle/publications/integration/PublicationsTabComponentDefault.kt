package com.sedsoftware.tackle.publications.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.PublicationsTabComponent.Model

@Suppress("UnusedPrivateProperty")
class PublicationsTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (ComponentOutput) -> Unit,
) : PublicationsTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Publications tab component")
    )
}
