package com.sedsoftware.tackle.status.alternatetext

import com.arkivanov.decompose.value.Value

interface AlternateTextComponent {
    val model: Value<Model>

    fun onDismiss()

    data class Model(
        val text: String,
    )
}
