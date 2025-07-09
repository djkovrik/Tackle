package com.sedsoftware.tackle.main.viewvideo.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent.Model

class ViewVideoComponentPreview(
    attachment: MediaAttachment,
) : ViewVideoComponent {
    override val model: Value<Model> = MutableValue(Model(attachment))
    override fun onBack() = Unit
}
