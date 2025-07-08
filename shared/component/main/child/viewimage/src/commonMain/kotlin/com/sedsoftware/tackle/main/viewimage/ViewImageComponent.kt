package com.sedsoftware.tackle.main.viewimage

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment

interface ViewImageComponent {
    val model: Value<Model>

    fun onBack()

    data class Model(
        val attachments: List<MediaAttachment>,
        val selectedIndex: Int,
    )
}
