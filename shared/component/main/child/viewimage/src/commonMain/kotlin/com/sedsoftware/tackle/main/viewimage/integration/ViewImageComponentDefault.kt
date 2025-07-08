package com.sedsoftware.tackle.main.viewimage.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewimage.ViewImageComponent
import com.sedsoftware.tackle.main.viewimage.ViewImageComponent.Model

class ViewImageComponentDefault(
    private val componentContext: ComponentContext,
    private val attachments: List<MediaAttachment>,
    private val selectedIndex: Int,
    private val onBackClicked: () -> Unit,
) : ViewImageComponent, ComponentContext by componentContext {

    override val model: Value<Model> =
        MutableValue(Model(attachments, selectedIndex))

    override fun onBack() {
        onBackClicked()
    }
}
