package com.sedsoftware.tackle.explore

import com.arkivanov.decompose.value.Value

interface ExploreTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
