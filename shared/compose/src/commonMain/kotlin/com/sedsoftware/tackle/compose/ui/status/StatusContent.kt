package com.sedsoftware.tackle.compose.ui.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.ui.status.content.StatusAttachments
import com.sedsoftware.tackle.compose.ui.status.content.StatusPoll
import com.sedsoftware.tackle.compose.ui.status.content.StatusPreviewCard
import com.sedsoftware.tackle.compose.ui.status.content.StatusText
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.PreviewCard
import com.sedsoftware.tackle.status.StatusComponent

@Composable
internal fun StatusContent(
    component: StatusComponent,
    modifier: Modifier = Modifier,
) {
    val model: StatusComponent.Model by component.model.subscribeAsState()
    val showExtendedInfo: Boolean = remember { model.status.editedAt != null }

    StatusContentWrapper(
        model = model,
        modifier = modifier,
        showExtendedInfo = showExtendedInfo,
        onFavouriteClick = component::onFavouriteClick,
        onReblogClick = component::onReblogClick,
        onMenuRequest = component::onMenuRequest,
        onMenuActionClick = component::onMenuActionClick,
        onReplyClick = TODO("Reply"),
        onShareClick = TODO("Share"),
    ) {

        if (model.status.content.isNotEmpty()) {
            StatusText(
                model = model,
                modifier = modifier,
                onHashTagClick = TODO("onHashTagClick"),
                onMentionClick = TODO("onMentionClick"),
                onUrlClick = TODO("onUrlClick"),
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
            StatusAttachments(
                attachments = model.status.mediaAttachments,
                onDownloadClick = TODO("onDownloadClick"),
                onCancelClick = TODO("onCancelClick"),
                onDoneClick = TODO("onDoneClick"),
                modifier = modifier,
            )
        }

        model.status.card?.let { previewCard: PreviewCard ->
            StatusPreviewCard(
                card = previewCard,
                onUrlClick = TODO("onUrlClick"),
                modifier = modifier,
            )
        }
    }
}
