package com.sedsoftware.tackle.explore.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.explore.ExploreTabComponent.Model

class ExploreTabComponentPreview : ExploreTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Explore tab component")
    )
}
