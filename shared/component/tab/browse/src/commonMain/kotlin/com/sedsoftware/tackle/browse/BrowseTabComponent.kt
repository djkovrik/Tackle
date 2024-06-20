package com.sedsoftware.tackle.browse

import com.arkivanov.decompose.value.Value

interface BrowseTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
