package com.sedsoftware.tackle.statuslist

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.status.StatusComponent

@OptIn(ExperimentalDecomposeApi::class)
interface StatusListComponent {

    val model: Value<Model>
    val components: MutableValue<List<StatusComponent>>

    fun onPullToRefresh()
    fun onLoadMoreRequest()

    data class Model(
        val initialProgressVisible: Boolean,
        val loadMoreProgressVisible: Boolean,
        val emptyPlaceholderVisible: Boolean,
    )
}
