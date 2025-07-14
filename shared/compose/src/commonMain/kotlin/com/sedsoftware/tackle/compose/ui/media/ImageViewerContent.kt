package com.sedsoftware.tackle.compose.ui.media

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.widget.TackleIconButton
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewimage.ViewImageComponent
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
internal fun ImageViewerContent(
    component: ViewImageComponent,
    modifier: Modifier = Modifier,
) {
    val model: ViewImageComponent.Model by component.model.subscribeAsState()

    var displayedAttachment: MediaAttachment by remember { mutableStateOf(model.attachments[model.selectedIndex]) }

    val lazyListState: LazyListState = rememberLazyListState()
    val visibleItemIndex: Int by lazyListState.visibleItemIndex()

    LaunchedEffect(Unit) {
        lazyListState.scrollToItem(model.selectedIndex)
    }

    LaunchedEffect(visibleItemIndex) {
        if (visibleItemIndex != -1) {
            displayedAttachment = model.attachments[visibleItemIndex]
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    TackleIconButton(
                        iconRes = Res.drawable.editor_close,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = component::onBack,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.surfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                actions = {},
                modifier = modifier,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    ) { paddingValues: PaddingValues ->

        Box(
            modifier = modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            LazyRow(
                state = lazyListState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
                modifier = modifier.align(alignment = Alignment.Center),
            ) {
                itemsIndexed(items = model.attachments) { index: Int, attachment ->
                    val displayedAttachmentParams: TackleImageParams = remember {
                        TackleImageParams(
                            blurhash = attachment.blurhash,
                            ratio = attachment.meta?.small?.aspect
                                ?: attachment.meta?.original?.aspect
                                ?: 1f,
                        )
                    }

                    TackleImage(
                        imageUrl = attachment.previewUrl,
                        imageParams = displayedAttachmentParams,
                        contentDescription = attachment.description,
                        showProgress = true,
                        progressSize = 32.dp,
                        modifier = modifier
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                            .fillParentMaxWidth()
                    )
                }
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp, max = 160.dp)
                    .verticalScroll(state = rememberScrollState())
                    .align(alignment = Alignment.BottomCenter)
                    .animateContentSize()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = displayedAttachment.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun LazyListState.visibleItemIndex(): State<Int> {
    return remember(this) {
        derivedStateOf {
            val layoutInfo = layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            val firstItem = visibleItemsInfo.firstOrNull()

            val index = firstItem?.index ?: -1
            index
        }
    }
}
