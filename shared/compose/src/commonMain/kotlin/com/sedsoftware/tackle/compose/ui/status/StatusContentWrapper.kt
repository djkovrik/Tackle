package com.sedsoftware.tackle.compose.ui.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.extension.getSimplifiedDate
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.compose.widget.TackleStatusButton
import com.sedsoftware.tackle.domain.model.Status
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_time_now
import tackle.shared.compose.generated.resources.status_favorite
import tackle.shared.compose.generated.resources.status_info_created
import tackle.shared.compose.generated.resources.status_info_edited
import tackle.shared.compose.generated.resources.status_more
import tackle.shared.compose.generated.resources.status_reblog
import tackle.shared.compose.generated.resources.status_reply
import tackle.shared.compose.generated.resources.status_share

@Composable
private fun StatusContentWrapper(
    status: Status,
    modifier: Modifier = Modifier,
    showExtendedInfo: Boolean = false,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    content: @Composable () -> Unit = {},
) {
    val simplifiedDate: Pair<Int, StringResource> = remember { status.createdAtShort.getSimplifiedDate() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        ) {
            // Avatar
            TackleImage(
                data = status.account.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size = 54.dp)
                    .clip(shape = CircleShape)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(weight = 1f, fill = true)
            ) {
                // Name
                Text(
                    text = status.account.displayName.takeIf { it.isNotEmpty() } ?: status.account.username,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier,
                ) {
                    // Visibility
                    Image(
                        painter = painterResource(resource = status.visibility.getIcon()),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = contentColor.copy(
                            alpha = 0.75f
                        )),
                        modifier = Modifier.size(size = 14.dp),
                    )

                    // Tag
                    Text(
                        text = "@${status.account.acct}",
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
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            // More button
            TackleStatusButton(
                iconRes = Res.drawable.status_more,
                color = contentColor,
                counter = 0,
                onClick = {},
            )
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
                        text = status.createdAtPretty,
                        color = contentColor,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp),
                    )

                    Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                    // App name
                    if (!status.application?.name.isNullOrEmpty()) {
                        Text(
                            text = status.application?.name.orEmpty(),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            modifier = Modifier,
                        )
                    }
                }

                // Edited
                if (status.editedAtPretty != null) {
                    Row {
                        Text(
                            text = "${stringResource(resource = Res.string.status_info_edited)}:",
                            color = contentColor,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            modifier = Modifier,
                        )

                        Text(
                            text = status.editedAtPretty.orEmpty(),
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
                counter = status.repliesCount,
                color = contentColor,
                onClick = {},
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_reblog,
                counter = status.reblogsCount,
                color = contentColor,
                onClick = {},
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_favorite,
                counter = status.favouritesCount,
                color = contentColor,
                onClick = {},
            )

            TackleStatusButton(
                iconRes = Res.drawable.status_share,
                counter = 0,
                color = contentColor,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun StatusContentWrapperPreviewLight() {
    TackleScreenPreview {
        StatusContentWrapperPreviewContent(PreviewStubs.statusNormal)
    }
}

@Preview
@Composable
private fun StatusContentWrapperPreviewLongName() {
    TackleScreenPreview {
        StatusContentWrapperPreviewContent(PreviewStubs.statusWithLongNames)
    }
}


@Preview
@Composable
private fun StatusContentWrapperPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        StatusContentWrapperPreviewContent(PreviewStubs.statusNormal)
    }
}

@Composable
private fun StatusContentWrapperPreviewContent(status: Status) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        StatusContentWrapper(
            status = status,
            showExtendedInfo = false,
        ) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .height(height = 120.dp)
            )
        }

        HorizontalDivider()

        StatusContentWrapper(
            status = status,
            showExtendedInfo = true,
        ) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .height(height = 120.dp)
            )
        }

        HorizontalDivider()
    }
}
