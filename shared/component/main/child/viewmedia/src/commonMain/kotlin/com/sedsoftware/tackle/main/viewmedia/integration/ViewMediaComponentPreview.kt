package com.sedsoftware.tackle.main.viewmedia.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent.Model
import io.github.vinceglb.filekit.PlatformFile

class ViewMediaComponentPreview(
    attachment: MediaAttachment,
    progress: Float,
) : ViewMediaComponent {
    override val model: Value<Model> = MutableValue(
        Model(
            attachments = listOf(attachment),
            selectedIndex = 0,
            downloadInProgress = listOf(progress > 0f && progress < 1f),
            downloadProgress = listOf(progress),
            downloadCompleted = listOf(progress == 1f),
        )
    )

    override fun onBack() = Unit
    override fun onDownload(destination: PlatformFile) = Unit
    override fun onSelectNewItem(index: Int) = Unit
}
