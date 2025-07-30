package com.sedsoftware.tackle.main.viewmedia

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment

interface ViewMediaComponent {
    val model: Value<Model>

    fun onBack()
    fun onDownload()

    data class Model(
        val attachments: List<MediaAttachment>,
        val selectedIndex: Int,
    )
}
