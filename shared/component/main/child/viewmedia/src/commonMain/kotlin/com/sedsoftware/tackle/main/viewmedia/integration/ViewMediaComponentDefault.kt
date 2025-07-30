package com.sedsoftware.tackle.main.viewmedia.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent.Model

class ViewMediaComponentDefault(
    private val componentContext: ComponentContext,
    private val attachments: List<MediaAttachment>,
    private val selectedIndex: Int,
    private val onBackClicked: () -> Unit,
) : ViewMediaComponent, ComponentContext by componentContext {

    override val model: Value<Model> =
        MutableValue(Model(attachments, selectedIndex))

    override fun onBack() {
        onBackClicked()
    }

    override fun onDownload() {
        TODO("Not yet implemented")
    }
}
