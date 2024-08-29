package com.sedsoftware.tackle.profile.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.profile.ProfileTabComponent
import com.sedsoftware.tackle.profile.ProfileTabComponent.Model

@Suppress("UnusedPrivateProperty")
class ProfileTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (ComponentOutput) -> Unit,
) : ProfileTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Profile tab component")
    )
}
