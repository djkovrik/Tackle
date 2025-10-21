package com.sedsoftware.tackle.statuslist

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.main.StatusComponent

@OptIn(ExperimentalDecomposeApi::class)
interface StatusListComponent {

    val model: Value<Model>

    val items: LazyChildItems<Status, StatusComponent>

    fun onPullToRefresh()
    fun onLoadMoreRequest()
    fun showCreatedStatus(status: Status)

    data class Model(
        val initialProgressVisible: Boolean,
        val loadMoreProgressVisible: Boolean,
        val emptyPlaceholderVisible: Boolean,
    )
}
