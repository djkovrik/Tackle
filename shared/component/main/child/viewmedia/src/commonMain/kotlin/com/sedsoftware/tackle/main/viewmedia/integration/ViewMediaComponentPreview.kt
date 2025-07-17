package com.sedsoftware.tackle.main.viewmedia.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent.Model

class ViewMediaComponentPreview(
    attachments: List<MediaAttachment>,
    selectedIndex: Int,
) : ViewMediaComponent {
    override val model: Value<Model> = MutableValue(Model(attachments, selectedIndex))
    override fun onBack() = Unit
}
