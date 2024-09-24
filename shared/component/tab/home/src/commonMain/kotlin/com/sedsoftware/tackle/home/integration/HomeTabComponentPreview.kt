package com.sedsoftware.tackle.home.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.HomeTabComponent.Model

class HomeTabComponentPreview : HomeTabComponent {

    override fun onNewPostClick() = Unit
    override fun onScheduledPostsClick() = Unit

    override val model: Value<Model> = MutableValue(
        Model(text = "Home tab component")
    )
}
