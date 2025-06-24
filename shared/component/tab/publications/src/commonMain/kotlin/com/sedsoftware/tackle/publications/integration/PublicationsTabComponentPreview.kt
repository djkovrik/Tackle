package com.sedsoftware.tackle.publications.integration

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentPreview

class PublicationsTabComponentPreview(
    statuses: List<Status> = emptyList(),
) : PublicationsTabComponent {

    override val childStack: Value<ChildStack<*, PublicationsTabComponent.Child>> =
        MutableValue(
            ChildStack(
                active = Child.Created(
                    configuration = "0",
                    instance = PublicationsTabComponent.Child.LocalTimeline(
                        component = StatusListComponentPreview(statuses)
                    )
                )
            )
        )

    override fun onLocalTabClick() = Unit
    override fun onRemoteTabClick() = Unit
}
