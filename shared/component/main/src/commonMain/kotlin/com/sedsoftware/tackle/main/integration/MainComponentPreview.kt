package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Model

class MainComponentPreview : MainComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Main component")
    )
}
