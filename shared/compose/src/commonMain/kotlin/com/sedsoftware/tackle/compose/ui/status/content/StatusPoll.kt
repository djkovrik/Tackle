package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.compose.widget.TackleCheckbox
import com.sedsoftware.tackle.compose.widget.TackleStatusRichText
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Poll
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_poll_active_till
import tackle.shared.compose.generated.resources.status_poll_completed
import tackle.shared.compose.generated.resources.status_poll_voters

@Composable
@Suppress("UnusedParameter")
internal fun StatusPoll(
    poll: Poll,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
) {
    Column(modifier = modifier) {
        if (!poll.expired) {
            Text(
                text = "${stringResource(Res.string.status_poll_active_till)} ${poll.expiresAtStr}",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        } else {
            Text(
                text = stringResource(Res.string.status_poll_completed),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }

        poll.options.forEachIndexed { index, option ->
            StatusPollOption(
                title = option.title,
                votesCount = option.votesCount,
                votesTotal = poll.votesCount,
                emojis = poll.emojis,
                expired = poll.expired,
                voted = poll.voted,
                hideTotals = poll.hideTotals,
                chosen = poll.ownVotes.contains(index),
            )
        }

        Text(
            text = "${stringResource(Res.string.status_poll_voters)} ${poll.votersCount}",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

private const val POLL_MIN_FRACTION = 0.15f

@Composable
private fun StatusPollOption(
    title: String,
    votesCount: Long,
    votesTotal: Long,
    emojis: List<CustomEmoji>,
    expired: Boolean,
    voted: Boolean,
    hideTotals: Boolean,
    chosen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)
    ) {
        val fraction = (votesCount.toFloat() / votesTotal.toFloat()).takeIf { it > POLL_MIN_FRACTION }
            ?: POLL_MIN_FRACTION

        Box(
            modifier = modifier
                .fillMaxWidth(fraction = fraction)
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .clip(shape = MaterialTheme.shapes.extraLarge)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.extraLarge,
                )
                .clickable(onClick = onClick)
                .clip(shape = MaterialTheme.shapes.extraLarge),
        ) {
            TackleCheckbox(
                checked = chosen,
                expired = expired,
                size = 24.dp,
                borderWidth = 1.dp,
                backgroundColorUnchecked = Color.Transparent,
                backgroundColorChecked = MaterialTheme.colorScheme.tertiary,
                borderColorUnchecked = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.75f),
                borderColorChecked = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(all = 12.dp)
            )

            TackleStatusRichText(
                content = title,
                emojis = emojis,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(weight = 1f, fill = true),
                inlinedContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Red)
                    )
                }
            )

            if (!hideTotals && voted) {
                Text(
                    text = votesCount.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatusPollPreviewLight() {
    TackleScreenPreview {
        StatusPollContent()
    }
}

@Preview
@Composable
private fun StatusPollPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        StatusPollContent()
    }
}

@Composable
private fun StatusPollContent() {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            )
    ) {
        StatusPoll(poll = PreviewStubs.pollNotVoted)
        Spacer(
            modifier = Modifier
                .height(height = 32.dp).fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        )
        StatusPoll(poll = PreviewStubs.pollNotVotedExpired)
        Spacer(
            modifier = Modifier
                .height(height = 32.dp).fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        )
        StatusPoll(poll = PreviewStubs.pollVoted)
        Spacer(
            modifier = Modifier
                .height(height = 32.dp).fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        )
        StatusPoll(poll = PreviewStubs.pollVotedExpired)
    }
}
