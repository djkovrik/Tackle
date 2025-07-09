package com.sedsoftware.tackle.compose.ui.media

import VideoPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.widget.TackleIconButton
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close

@Composable
internal fun VideoViewerContent(
    component: ViewVideoComponent,
    modifier: Modifier = Modifier,
) {
    val model: ViewVideoComponent.Model by component.model.subscribeAsState()

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
            VideoPlayer(
                url = model.attachment.url,
                showControls = true,
                autoPlay = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = model.attachment.meta?.small?.aspect ?: 1f)
                    .align(alignment = Alignment.Center),
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp, max = 160.dp)
                    .verticalScroll(state = rememberScrollState())
                    .align(alignment = Alignment.BottomCenter)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = model.attachment.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
        }
    }
}
