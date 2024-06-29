package com.sedsoftware.tackle.notifications

import com.arkivanov.decompose.value.Value

interface NotificationsTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )
}
