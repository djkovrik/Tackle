package com.sedsoftware.tackle.home.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.home.HomeComponent
import com.sedsoftware.tackle.home.HomeComponent.Model

class HomeComponentPreview : HomeComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Home component")
    )
}
