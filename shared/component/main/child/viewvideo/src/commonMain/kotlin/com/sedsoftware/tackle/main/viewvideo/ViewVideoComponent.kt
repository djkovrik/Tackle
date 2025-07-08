package com.sedsoftware.tackle.main.viewvideo

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment

interface ViewVideoComponent {
    val model: Value<Model>

    fun onBack()

    data class Model(
        val attachment: MediaAttachment,
    )
}
