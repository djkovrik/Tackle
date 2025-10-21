package com.sedsoftware.tackle.compose.ui.media

import VideoPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.ui.CompositionLocalProviders.LocalFileKitDialogSettings
import com.sedsoftware.tackle.compose.ui.CompositionLocalProviders.LocalNavAnimatedVisibilityScope
import com.sedsoftware.tackle.compose.ui.CompositionLocalProviders.LocalSharedTransitionScope
import com.sedsoftware.tackle.compose.widget.TackleAppBarButton
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.openFileSaver
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.attachment_download_alt
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.status_content_description_back
import tackle.shared.compose.generated.resources.status_content_description_download
import tackle.shared.compose.generated.resources.status_content_downloaded

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
internal fun VideoViewerContent(
    component: ViewMediaComponent,
    modifier: Modifier = Modifier,
) {
    val model: ViewMediaComponent.Model by component.model.subscribeAsState()
    val downloadInProgress = model.downloadInProgress[model.selectedIndex]
    val downloadCompleted = model.downloadCompleted[model.selectedIndex]
    val currentProgress = model.downloadProgress[model.selectedIndex]
    val displayedAttachment: MediaAttachment by remember { mutableStateOf(model.attachments[model.selectedIndex]) }

    val sharedTransitionScope: SharedTransitionScope =
        LocalSharedTransitionScope.current ?: error("No SharedElementScope found")

    val animatedVisibilityScope: AnimatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current ?: error("No AnimatedVisibility found")

    val dialogSettings: FileKitDialogSettings =
        LocalFileKitDialogSettings.current ?: error("No dialog settings found")

    val fullName = remember { displayedAttachment.url.substringAfterLast("/") }
    val (name, extension) = fullName.split(".")
    var dialogVisible: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(dialogVisible) {
        if (dialogVisible) {
            val file: PlatformFile? = FileKit.openFileSaver(
                suggestedName = name,
                extension = extension,
                dialogSettings = dialogSettings,
            )

            if (file != null) {
                component.onDownload(file)
            }

            delay(DIALOG_DEBOUNCE_MS)
            dialogVisible = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    TackleAppBarButton(
                        iconRes = Res.drawable.editor_close,
                        contentDescriptionRes = Res.string.status_content_description_back,
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
                actions = {
                    TackleAppBarButton(
                        iconRes = Res.drawable.attachment_download_alt,
                        contentDescriptionRes = Res.string.status_content_description_download,
                        enabled = !downloadInProgress && !downloadCompleted,
                        onClick = { dialogVisible = true },
                    )
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.TopCenter)
                    .animateContentSize()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                AnimatedVisibility(visible = downloadInProgress || downloadCompleted) {
                    LinearProgressIndicator(
                        progress = { currentProgress },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }

                AnimatedVisibility(visible = downloadCompleted) {
                    Text(
                        text = stringResource(resource = Res.string.status_content_downloaded),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    )
                }
            }

            with(sharedTransitionScope) {
                VideoPlayer(
                    url = displayedAttachment.url,
                    showControls = false,
                    autoPlay = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = displayedAttachment.meta?.small?.aspect ?: 1f)
                        .align(alignment = Alignment.Center)
                        .sharedBounds(
                            rememberSharedContentState(key = displayedAttachment.id),
                            animatedVisibilityScope = animatedVisibilityScope,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                            renderInOverlayDuringTransition = true,
                        )
                        .clip(shape = MaterialTheme.shapes.extraSmall),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp, max = 160.dp)
                    .verticalScroll(state = rememberScrollState())
                    .align(alignment = Alignment.BottomCenter)
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

private const val DIALOG_DEBOUNCE_MS = 250L
