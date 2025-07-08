package com.sedsoftware.tackle.main.viewvideo.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent.Model

class ViewVideoComponentDefault(
    private val componentContext: ComponentContext,
    private val attachment: MediaAttachment,
    private val onBackClicked: () -> Unit,
) : ViewVideoComponent, ComponentContext by componentContext {

    override val model: Value<Model> =
        MutableValue(Model(attachment))

    override fun onBack() {
        onBackClicked()
    }
}
