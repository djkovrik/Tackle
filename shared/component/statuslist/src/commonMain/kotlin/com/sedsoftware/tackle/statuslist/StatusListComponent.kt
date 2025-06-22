package com.sedsoftware.tackle.statuslist

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.status.StatusComponent

interface StatusListComponent {

    val model: Value<Model>
    val components: Value<List<StatusComponent>>

    fun onPullToRefresh()
    fun onLoadMoreRequest()
    fun showCreatedStatus(status: Status)

    data class Model(
        val initialProgressVisible: Boolean,
        val loadMoreProgressVisible: Boolean,
        val emptyPlaceholderVisible: Boolean,
    )
}
