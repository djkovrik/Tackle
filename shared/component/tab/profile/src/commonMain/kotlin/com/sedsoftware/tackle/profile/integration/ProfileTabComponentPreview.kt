package com.sedsoftware.tackle.profile.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.profile.ProfileTabComponent
import com.sedsoftware.tackle.profile.ProfileTabComponent.Model

class ProfileTabComponentPreview : ProfileTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Profile tab component")
    )
}
