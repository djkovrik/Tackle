package com.sedsoftware.tackle.home

import com.arkivanov.decompose.value.Value

interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
