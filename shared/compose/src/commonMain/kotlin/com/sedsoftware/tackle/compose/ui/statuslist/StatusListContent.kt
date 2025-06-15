package com.sedsoftware.tackle.compose.ui.statuslist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.status.StatusContent
import com.sedsoftware.tackle.compose.ui.status.StatusPreviewStubs
import com.sedsoftware.tackle.compose.ui.statuslist.content.StatusLoadMoreIndicator
import com.sedsoftware.tackle.compose.ui.statuslist.observer.LazyListItemsVisibilityObserver
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusListContent(
    component: StatusListComponent,
    modifier: Modifier = Modifier,
) {
    val model: StatusListComponent.Model by component.model.subscribeAsState()
    val components: List<StatusComponent> by component.components.subscribeAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val pullToRefreshState: PullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = model.initialProgressVisible,
        onRefresh = component::onPullToRefresh,
        modifier = modifier,
        state = pullToRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = model.initialProgressVisible,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                state = pullToRefreshState
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            items(items = components, key = { it.getId() }) { component: StatusComponent ->
                StatusContent(component)
            }

            if (model.loadMoreProgressVisible) {
                item(key = "Load more indicator") {
                    StatusLoadMoreIndicator()
                }
            }
        }
    }

    LazyListItemsVisibilityObserver(
        component = component,
        lazyListState = lazyListState,
        forwardPreloadCount = 1,
        backwardPreloadCount = 1,
        loadMoreCallbackThreshold = 1,
        onLoadMoreCallback = component::onLoadMoreRequest,
    )
}

@Preview
@Composable
private fun StatusListContentInitialLoadingPreview() {
    TackleScreenPreview {
        StatusListContent(
            component = StatusListComponentPreview(
                initialProgressVisible = true,
            )
        )
    }
}

@Preview
@Composable
private fun StatusListContentNoLoadingPreview() {
    TackleScreenPreview {
        StatusListContent(
            component = StatusListComponentPreview(
                statuses = listOf(
                    StatusPreviewStubs.status.copy(id = "1"),
                    StatusPreviewStubs.statusWithLongTexts.copy(id = "2"),
                    StatusPreviewStubs.status.copy(id = "3"),
                )
            )
        )
    }
}

@Preview
@Composable
private fun StatusListContentLoadMorePreview() {
    TackleScreenPreview {
        StatusListContent(
            component = StatusListComponentPreview(
                statuses = listOf(
                    StatusPreviewStubs.status.copy(id = "1"),
                    StatusPreviewStubs.statusWithLongTexts.copy(id = "2"),
                    StatusPreviewStubs.status.copy(id = "3"),
                ),
                loadMoreProgressVisible = true,
            )
        )
    }
}
