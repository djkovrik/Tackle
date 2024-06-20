package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.main.MainComponent

class MainComponentPreview(
    child: MainComponent.Child
) : MainComponent {

    override val childStack: Value<ChildStack<*, MainComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance = child,
            )
        )

    override fun onHomeTabClicked() = Unit
    override fun onExploreTabClicked() = Unit
    override fun onEditorTabClicked() = Unit
    override fun onPublicationsTabClicked() = Unit
    override fun onNotificationsTabClicked() = Unit
}
