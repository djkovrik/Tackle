package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.widget.TackleButton
import com.sedsoftware.tackle.compose.widget.TackleCheckbox
import com.sedsoftware.tackle.compose.widget.TackleStatusRichText
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.Translation
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_poll_active_till
import tackle.shared.compose.generated.resources.status_poll_button
import tackle.shared.compose.generated.resources.status_poll_completed
import tackle.shared.compose.generated.resources.status_poll_voters

@Composable
internal fun StatusPoll(
    poll: Poll,
    translation: Translation?,
    onSelect: (Int, Boolean) -> Unit,
    onVote: () -> Unit,
    modifier: Modifier = Modifier,
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
                title = if (translation?.poll?.options?.size == poll.options.size) {
                    translation.poll?.options?.get(index)?.title ?: option.title
                } else {
                    option.title
                },
                votesCount = option.votesCount,
                votesTotal = poll.votesCount,
                emojis = poll.emojis,
                expired = poll.expired,
                voted = poll.voted,
                multiselect = poll.multiple,
                hideTotals = poll.hideTotals,
                chosen = poll.ownVotes.contains(index),
                onClick = {
                    if (!poll.expired && !poll.voted) {
                        onSelect.invoke(index, poll.multiple)
                    }
                },
            )
        }

        Text(
            text = "${stringResource(Res.string.status_poll_voters)} ${poll.votersCount}",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        if (!poll.expired && !poll.voted) {
            TackleButton(
                text = stringResource(resource = Res.string.status_poll_button),
                onClick = onVote,
                enabled = poll.ownVotes.isNotEmpty() && !poll.voted,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

private const val POLL_MIN_FRACTION = 0.15f
private const val POLL_ANIMATION_DURATION = 500

@Composable
private fun StatusPollOption(
    title: String,
    votesCount: Long,
    votesTotal: Long,
    emojis: List<CustomEmoji>,
    expired: Boolean,
    voted: Boolean,
    multiselect: Boolean,
    hideTotals: Boolean,
    chosen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)
    ) {
        val optionFraction = (votesCount.toFloat() / votesTotal.toFloat()).takeIf { it > POLL_MIN_FRACTION }
            ?: POLL_MIN_FRACTION

        val currentFraction: Float by animateFloatAsState(
            targetValue = if (voted && votesCount != 0L) optionFraction else 0f,
            animationSpec = tween(durationMillis = POLL_ANIMATION_DURATION, easing = FastOutSlowInEasing),
        )

        Box(
            modifier = modifier
                .fillMaxWidth(fraction = currentFraction)
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = if (multiselect) {
                        MaterialTheme.shapes.extraSmall
                    } else {
                        MaterialTheme.shapes.extraLarge
                    }
                )
                .clip(shape = if (multiselect) {
                    MaterialTheme.shapes.extraSmall
                } else {
                    MaterialTheme.shapes.extraLarge
                })
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                    shape = if (multiselect) {
                        MaterialTheme.shapes.extraSmall
                    } else {
                        MaterialTheme.shapes.extraLarge
                    },
                )
                .clip(
                    shape = if (multiselect) {
                    MaterialTheme.shapes.extraSmall
                } else {
                    MaterialTheme.shapes.extraLarge
                }),
        ) {
            TackleCheckbox(
                checked = chosen,
                voted = voted,
                expired = expired,
                multiselect = multiselect,
                size = 24.dp,
                borderWidth = 1.dp,
                backgroundColorUnchecked = Color.Transparent,
                backgroundColorChecked = MaterialTheme.colorScheme.tertiary,
                borderColorUnchecked = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.75f),
                borderColorChecked = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(all = 12.dp),
                onClick = onClick,
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
                        contentAlignment = Alignment.Center,
                        modifier = modifier.fillMaxSize(),
                    ) {
                        Image(
                            painter = rememberImagePainter(url = it),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = modifier.fillMaxSize(),
                        )
                    }
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
