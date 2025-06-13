package com.sedsoftware.tackle.compose.ui.status

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.extension.getSimplifiedDate
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.compose.widget.TackleStatusButton
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.model.StatusContextAction
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_time_now
import tackle.shared.compose.generated.resources.status_bookmarked
import tackle.shared.compose.generated.resources.status_boosted
import tackle.shared.compose.generated.resources.status_favorite
import tackle.shared.compose.generated.resources.status_info_created
import tackle.shared.compose.generated.resources.status_info_edited
import tackle.shared.compose.generated.resources.status_more
import tackle.shared.compose.generated.resources.status_muted
import tackle.shared.compose.generated.resources.status_pinned
import tackle.shared.compose.generated.resources.status_reblog
import tackle.shared.compose.generated.resources.status_reply
import tackle.shared.compose.generated.resources.status_share

@Composable
internal fun StatusContentWrapper(
    model: StatusComponent.Model,
    modifier: Modifier = Modifier,
    showExtendedInfo: Boolean = false,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    onReplyClick: () -> Unit = {},
    onFavouriteClick: () -> Unit = {},
    onReblogClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onMenuRequest: (Boolean) -> Unit = {},
    onMenuActionClick: (StatusContextAction) -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val simplifiedDate: Pair<Int, StringResource> = remember { model.status.createdAtShort.getSimplifiedDate() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        if (model.rebloggedBy.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp),
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.status_reblog),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.size(size = 12.dp),
                )

                Text(
                    text = "${model.rebloggedBy} ${stringResource(resource = Res.string.status_boosted)}",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }

        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        ) {
            // Avatar
            TackleImage(
                data = model.status.account.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size = 54.dp)
                    .clip(shape = CircleShape)
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(weight = 1f, fill = true)
            ) {
                // Name
                Text(
                    text = model.status.account.displayName.takeIf { it.isNotEmpty() }
                        ?: model.status.account.username,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier,
                ) {
                    // Visibility
                    Image(
                        painter = painterResource(resource = model.status.visibility.getIcon()),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            color = contentColor.copy(
                                alpha = 0.75f
                            )
                        ),
                        modifier = Modifier.size(size = 14.dp),
                    )

                    // Tag
                    Text(
                        text = "@${model.status.account.acct}",
                        color = contentColor.copy(
                            alpha = 0.75f,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )

                }
            }

            Column {
                AnimatedVisibility(
                    visible = model.status.pinned,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.status_pinned),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .alpha(alpha = 0.8f)
                            .size(size = 14.dp),
                    )
                }

                AnimatedVisibility(
                    visible = model.status.bookmarked,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.status_bookmarked),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .alpha(alpha = 0.8f)
                            .size(size = 14.dp),
                    )
                }

                AnimatedVisibility(
                    visible = model.status.muted,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.status_muted),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .alpha(alpha = 0.8f)
                            .size(size = 14.dp),
                    )
                }
            }

            // Time
            Text(
                text = if (simplifiedDate.first != -1) {
                    "${simplifiedDate.first}${stringResource(simplifiedDate.second)}"
                } else {
                    stringResource(Res.string.common_time_now)
                },
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
            )

            // More button
            TackleStatusButton(
                iconRes = Res.drawable.status_more,
                color = contentColor,
                counter = 0,
                onClick = { onMenuRequest.invoke(true) },
            )
            DropdownMenu(
                expanded = model.menuVisible,
                shape = MaterialTheme.shapes.small,
                onDismissRequest = { onMenuRequest.invoke(false) },
            ) {
                model.menuActions.forEach { menuAction: StatusContextAction ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(resource = menuAction.getTitle()),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier,
                            )
                        },
                        onClick = {
                            onMenuActionClick.invoke(menuAction)
                        }
                    )
                }
            }
        }

        content()

        // Extended info footer
        if (showExtendedInfo) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                // Created
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "${stringResource(resource = Res.string.status_info_created)}:",
                        color = contentColor,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        modifier = Modifier,
                    )
                    Text(
                        text = model.status.createdAtPretty,
                        color = contentColor,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp),
                    )

                    Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                    // App name
                    if (!model.status.application?.name.isNullOrEmpty()) {
                        Text(
                            text = model.status.application?.name.orEmpty(),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            modifier = Modifier,
                        )
                    }
                }

                // Edited
                if (model.status.editedAtPretty != null) {
                    Row {
                        Text(
                            text = "${stringResource(resource = Res.string.status_info_edited)}:",
                            color = contentColor,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            modifier = Modifier,
                        )

                        Text(
                            text = model.status.editedAtPretty.orEmpty(),
                            color = contentColor,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        }

        // Footer
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            TackleStatusButton(
                iconRes = Res.drawable.status_reply,
                counter = model.status.repliesCount,
                color = contentColor,
                onClick = onReplyClick,
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_reblog,
                counter = model.status.reblogsCount,
                color = contentColor,
                onClick = onReblogClick,
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_favorite,
                counter = model.status.favouritesCount,
                color = contentColor,
                onClick = onFavouriteClick,
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_share,
                counter = 0,
                color = contentColor,
                onClick = onShareClick,
            )
        }
    }
}
