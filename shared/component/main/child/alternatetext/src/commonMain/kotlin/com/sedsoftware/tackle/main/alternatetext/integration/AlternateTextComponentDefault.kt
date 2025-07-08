package com.sedsoftware.tackle.main.alternatetext.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent.Model

class AlternateTextComponentDefault(
    private val componentContext: ComponentContext,
    private val text: String,
    private val onDismissed: () -> Unit,
) : AlternateTextComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(Model(text = text))

    override fun onDismiss() {
        onDismissed()
    }
}
