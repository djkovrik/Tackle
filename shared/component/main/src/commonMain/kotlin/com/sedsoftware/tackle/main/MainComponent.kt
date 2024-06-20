package com.sedsoftware.tackle.main

import com.arkivanov.decompose.value.Value

interface MainComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
