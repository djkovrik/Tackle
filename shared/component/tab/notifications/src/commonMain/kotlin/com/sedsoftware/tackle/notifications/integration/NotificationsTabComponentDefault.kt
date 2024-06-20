package com.sedsoftware.tackle.notifications.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.notifications.NotificationsTabComponent.Model

class NotificationsTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (NotificationsTabComponent.Output) -> Unit,
) : NotificationsTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Notifications tab component")
    )
}
