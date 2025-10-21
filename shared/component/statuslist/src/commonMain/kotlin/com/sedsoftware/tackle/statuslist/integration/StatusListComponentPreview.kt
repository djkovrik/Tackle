package com.sedsoftware.tackle.statuslist.integration

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.main.StatusComponent
import com.sedsoftware.tackle.main.integration.StatusComponentPreview
import com.sedsoftware.tackle.statuslist.StatusListComponent

@OptIn(ExperimentalDecomposeApi::class)
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
            )
        )

    override val items: LazyChildItems<Status, StatusComponent> =
        SimpleLazyChildItems(
            items = statuses
                .map { status -> StatusComponentPreview(status = status) }
                .associateBy { component -> component.status }
        )

    override fun onPullToRefresh() = Unit
    override fun onLoadMoreRequest() = Unit
    override fun showCreatedStatus(status: Status) = Unit
}
