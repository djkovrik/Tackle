package com.sedsoftware.tackle.notifications.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.notifications.NotificationsTabComponent.Model

class NotificationsTabComponentPreview : NotificationsTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Notifications tab component")
    )
}
