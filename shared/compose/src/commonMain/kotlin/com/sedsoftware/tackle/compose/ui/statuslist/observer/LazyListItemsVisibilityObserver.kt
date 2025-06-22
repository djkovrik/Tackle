package com.sedsoftware.tackle.compose.ui.statuslist.observer

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent

@Composable
internal fun LazyListItemsVisibilityObserver(
    component: StatusListComponent,
    lazyListState: LazyListState,
    forwardPreloadCount: Int = 0,
    backwardPreloadCount: Int = 0,
    loadMoreCallbackThreshold: Int = 0,
    onLoadMoreCallback: () -> Unit = {},
) {
    val firstIndex: Int by remember(lazyListState) {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: -1
        }
    }

    val lastIndex: Int by remember(lazyListState) {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        }
    }

    val isScrolledToBottom: Boolean by remember(lazyListState) {
        derivedStateOf {
            if (lazyListState.layoutInfo.totalItemsCount != 0) {
                val layoutInfo = lazyListState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
                lastVisibleItem.index + loadMoreCallbackThreshold >= layoutInfo.totalItemsCount
            } else {
                false
            }
        }
    }

    LaunchedEffect(isScrolledToBottom) {
        if (isScrolledToBottom) {
            onLoadMoreCallback.invoke()
        }
    }

    val childComponents: List<StatusComponent> by component.components.subscribeAsState()

    DisposableEffect(childComponents, forwardPreloadCount, backwardPreloadCount, firstIndex, lastIndex) {
        if ((firstIndex >= 0) && (lastIndex >= 0)) {
            val range = firstIndex - backwardPreloadCount.coerceAtLeast(0)..lastIndex + forwardPreloadCount.coerceAtLeast(0)
            childComponents.forEachIndexed { index: Int, component: StatusComponent ->
                component.activateComponent(activate = index in range)
            }
        }

        onDispose {}
    }
}
