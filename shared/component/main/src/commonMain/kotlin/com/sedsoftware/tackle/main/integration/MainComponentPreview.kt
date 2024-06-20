package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.home.integration.HomeTabComponentPreview
import com.sedsoftware.tackle.main.MainComponent

class MainComponentPreview : MainComponent {

    override val childStack: Value<ChildStack<*, MainComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance = MainComponent.Child.TabHome(HomeTabComponentPreview()),
            )
        )

    override fun onHomeTabClicked() = Unit
    override fun onBrowseTabClicked() = Unit
    override fun onEditorTabClicked() = Unit
    override fun onFeedsTabClicked() = Unit
    override fun onNotificationsTabClicked() = Unit
}
