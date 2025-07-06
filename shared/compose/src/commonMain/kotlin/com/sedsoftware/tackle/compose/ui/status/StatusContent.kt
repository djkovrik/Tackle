package com.sedsoftware.tackle.compose.ui.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.github.panpf.sketch.rememberAsyncImagePainter
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.status.content.StatusAttachment
import com.sedsoftware.tackle.compose.ui.status.content.StatusPoll
import com.sedsoftware.tackle.compose.ui.status.content.StatusPreviewCard
import com.sedsoftware.tackle.compose.ui.status.content.StatusText
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.PreviewCard
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.integration.StatusComponentPreview
import com.sedsoftware.tackle.status.model.StatusAction
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusContent(
    component: StatusComponent,
    modifier: Modifier = Modifier,
    inlinedContent: @Composable (String) -> Unit = {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri = it),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier.fillMaxSize(),
            )
        }
    },
) {
    val model: StatusComponent.Model by component.model.subscribeAsState()

    StatusContentWrapper(
        model = model,
        modifier = modifier,
        showExtendedInfo = model.extendedInfo,
        onFavouriteClick = { component.onStatusAction(StatusAction.FAVOURITE) },
        onReblogClick = { component.onStatusAction(StatusAction.REBLOG) },
        onMenuRequest = { component.onMenuRequest(it) },
        onMenuActionClick = { component.onMenuActionClick(it) },
        onReplyClick = { component.onStatusAction(StatusAction.REPLY) },
        onShareClick = { component.onStatusAction(StatusAction.SHARE) },
    ) {

        if (model.status.content.isNotEmpty() || !model.translation?.content.isNullOrEmpty()) {
            StatusText(
                model = model,
                modifier = modifier,
                onHashTagClick = component::onHashTagClick,
                onMentionClick = component::onMentionClick,
                onUrlClick = component::onUrlClick,
                inlinedContent = inlinedContent,
            )
        }

        model.status.poll?.let { statusPoll: Poll ->
            StatusPoll(
                poll = statusPoll,
                translation = model.translation,
                onSelect = component::onPollSelect,
                onVote = component::onVoteClick,
                modifier = modifier,
            )
        }

        if (model.status.mediaAttachments.isNotEmpty()) {
            // TODO Attachment management
            StatusAttachment(
                attachments = model.status.mediaAttachments,
                hasSensitiveContent = model.status.sensitive,
                onContentClick = {},
                onContentAltClick = {},
                onDownloadClick = {},
                onCancelClick = {},
                modifier = modifier,
            )
        }

        model.status.card?.let { previewCard: PreviewCard ->
            StatusPreviewCard(
                card = previewCard,
                onUrlClick = component::onUrlClick,
                modifier = modifier,
            )
        }
    }
}

private const val TOGGLE_DARK_THEME = false

@Preview
@Composable
private fun PreviewStatusNormal() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.status,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusReblogged() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.status,
                rebloggedBy = "Someone else",
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusExtendedInfo() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.status,
                extendedInfo = true,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusTranslating() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithLongTexts,
                translationInProgress = true,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusTranslated() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithLongTexts,
                translationDisplayed = true,
                translation = Translation(
                    content = "Здесь какой-то переведенный текст.",
                    language = "ru",
                    sourceLanguage = "en",
                    provider = "DeepL",
                )
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusEmbeddedContent() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithEmbeddedContent,
            ),
            inlinedContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Red)
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithPollNotVoted() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPollNotVoted,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithPollNotVotedSelected() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPollNotVotedSelected,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithPollNotVotedExpired() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPollNotVotedExpired,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithPolVoted() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPollVoted,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithPolVotedExpired() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPollVotedExpired,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusPreviewCardImage() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPreviewCardImage,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusPreviewCardLink() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPreviewCardLink
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusPreviewCardVideo() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithPreviewCardVideo
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithImageAttachments() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithImageAttachments
            )
        )
    }
}

@Preview
@Composable
private fun PreviewStatusWithFileAttachments() {
    TackleScreenPreview(darkTheme = TOGGLE_DARK_THEME) {
        StatusContent(
            component = StatusComponentPreview(
                status = StatusPreviewStubs.statusWithFileAttachments
            )
        )
    }
}
