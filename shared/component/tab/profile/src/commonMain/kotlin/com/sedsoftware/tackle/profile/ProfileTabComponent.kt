package com.sedsoftware.tackle.profile

import com.arkivanov.decompose.value.Value

interface ProfileTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )
}
