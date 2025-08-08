package com.sedsoftware.tackle.main.viewmedia

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import io.github.vinceglb.filekit.PlatformFile

interface ViewMediaComponent {
    val model: Value<Model>

    fun onBack()
    fun onDownload(destination: PlatformFile)
    fun onSelectNewItem(index: Int)

    data class Model(
        val attachments: List<MediaAttachment>,
        val downloadInProgress: List<Boolean>,
        val downloadProgress: List<Float>,
        val downloadCompleted: List<Boolean>,
        val selectedIndex: Int,
    )
}
