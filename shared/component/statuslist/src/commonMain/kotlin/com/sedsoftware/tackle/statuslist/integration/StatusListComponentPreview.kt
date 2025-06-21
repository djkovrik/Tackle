package com.sedsoftware.tackle.statuslist.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.integration.StatusComponentPreview
import com.sedsoftware.tackle.statuslist.StatusListComponent

class StatusListComponentPreview(
    statuses: List<Status> = emptyList(),
    initialProgressVisible: Boolean = false,
    loadMoreProgressVisible: Boolean = false,
    emptyPlaceholderVisible: Boolean = false,
) : StatusListComponent {

    override val model: Value<StatusListComponent.Model> =
        MutableValue(
            initialValue = StatusListComponent.Model(
                initialProgressVisible = initialProgressVisible,
                loadMoreProgressVisible = loadMoreProgressVisible,
                emptyPlaceholderVisible = emptyPlaceholderVisible,
                scrollRequests = 0,
            )
        )

    override val components: MutableValue<List<StatusComponent>> =
        MutableValue(
            initialValue = statuses.map { StatusComponentPreview(status = it) }
        )

    override fun onPullToRefresh() = Unit
    override fun onLoadMoreRequest() = Unit
}
