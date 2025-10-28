@file:OptIn(ExperimentalDecomposeApi::class)

package com.sedsoftware.tackle.compose.ui.statuslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lazyitems.ChildItemsLifecycleController
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.items.ChildItems
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.status.StatusContent
import com.sedsoftware.tackle.compose.ui.status.StatusPreviewStubs
import com.sedsoftware.tackle.compose.ui.statuslist.content.StatusLoadMoreIndicator
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.main.StatusComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentPreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_home_timeline_empty

@Composable
internal fun StatusListContent(
    component: StatusListComponent,
    modifier: Modifier = Modifier,
) {
    val model: StatusListComponent.Model by component.model.subscribeAsState()
    val childItems: ChildItems<Status, StatusComponent> by component.items.subscribeAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val pullToRefreshState: PullToRefreshState = rememberPullToRefreshState()

    val isScrolledToBottom: Boolean by remember(lazyListState) {
        derivedStateOf {
            if (lazyListState.layoutInfo.totalItemsCount != 0) {
                val layoutInfo = lazyListState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
                lastVisibleItem.index + LOAD_MORE_THRESHOLD >= layoutInfo.totalItemsCount
            } else {
                false
            }
        }
    }

    LaunchedEffect(isScrolledToBottom) {
        if (isScrolledToBottom) {
            component.onLoadMoreRequest()
        }
    }

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
        AnimatedVisibility(
            visible = model.emptyPlaceholderVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = stringResource(resource = Res.string.status_home_timeline_empty),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 32.dp),
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            itemsIndexed(items = childItems.items, key = { _, status: Status -> status.id }) { index: Int, status: Status ->
                StatusContent(
                    component = component.items[status],
                )

                if (index < childItems.items.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .alpha(alpha = 0.1f)
                    )
                }

                AnimatedVisibility(
                    visible = model.loadMoreProgressVisible,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it },
                ) {
                    StatusLoadMoreIndicator()
                }
            }
        }
    }

    ChildItemsLifecycleController(
        items = component.items,
        lazyListState = lazyListState,
        forwardPreloadCount = 1,
        backwardPreloadCount = 1,
        itemIndexConverter = { it },
    )
}

private const val LOAD_MORE_THRESHOLD = 1

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
private fun StatusListContentEmptyPreview() {
    TackleScreenPreview {
        StatusListContent(
            component = StatusListComponentPreview(
                emptyPlaceholderVisible = true,
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
