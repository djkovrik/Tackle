package com.sedsoftware.tackle.browse.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.browse.BrowseTabComponent
import com.sedsoftware.tackle.browse.BrowseTabComponent.Model

class BrowseTabComponentPreview : BrowseTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Browse tab component")
    )
}
