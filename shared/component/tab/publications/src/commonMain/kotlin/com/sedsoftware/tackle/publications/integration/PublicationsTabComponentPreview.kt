package com.sedsoftware.tackle.publications.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.PublicationsTabComponent.Model

class PublicationsTabComponentPreview : PublicationsTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Publications tab component")
    )
}
