package com.sedsoftware.tackle.publications

import com.arkivanov.decompose.value.Value

interface PublicationsTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
