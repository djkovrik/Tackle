package com.sedsoftware.tackle.feeds

import com.arkivanov.decompose.value.Value

interface FeedsTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
