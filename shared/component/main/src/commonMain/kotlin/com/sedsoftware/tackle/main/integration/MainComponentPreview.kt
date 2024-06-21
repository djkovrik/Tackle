package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab

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

    override fun onTabClicked(tab: TackleNavigationTab) = Unit
}
