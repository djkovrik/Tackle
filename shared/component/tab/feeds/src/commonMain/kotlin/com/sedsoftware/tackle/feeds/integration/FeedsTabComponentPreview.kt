package com.sedsoftware.tackle.feeds.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.feeds.FeedsTabComponent
import com.sedsoftware.tackle.feeds.FeedsTabComponent.Model

class FeedsTabComponentPreview : FeedsTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Feeds tab component")
    )
}
