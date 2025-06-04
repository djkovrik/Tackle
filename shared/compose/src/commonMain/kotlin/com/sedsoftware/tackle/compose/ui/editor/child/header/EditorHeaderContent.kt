package com.sedsoftware.tackle.compose.ui.editor.child.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleIconButton
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_back
import tackle.shared.compose.generated.resources.editor_language
import tackle.shared.compose.generated.resources.editor_send
import tackle.shared.compose.generated.resources.editor_title

@Composable
internal fun EditorHeaderContent(
    model: EditorHeaderComponent.Model,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onLocalePickerRequest: () -> Unit = {},
    onVisibilityPickerRequest: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 128.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TackleIconButton(
                    iconRes = Res.drawable.editor_back,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = onBackClick,
                )

                Spacer(modifier = Modifier.width(width = 16.dp))

                Text(
                    text = stringResource(resource = Res.string.editor_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                )

                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                TackleIconButton(
                    iconRes = Res.drawable.editor_language,
                    additionalText = model.selectedLocale.languageCode,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    borderWidth = 1.dp,
                    onClick = onLocalePickerRequest,
                )

                Spacer(modifier = Modifier.width(width = 16.dp))

                TackleIconButton(
                    iconRes = Res.drawable.editor_send,
                    enabled = model.sendButtonAvailable,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onSendClick,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                TackleImage(
                    data = model.avatar,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size = 54.dp)
                        .clip(shape = CircleShape)
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = model.nickname,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = model.domain,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                TackleIconButton(
                    iconRes = model.statusVisibility.getIcon(),
                    additionalText = stringResource(resource = model.statusVisibility.getTitle()),
                    containerColor = containerColor,
                    contentColor = contentColor,
                    borderWidth = 1.dp,
                    onClick = onVisibilityPickerRequest,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditorHeaderContentPreviewLight() {
    TackleScreenPreview {
        EditorHeaderContent(
            model = EditorHeaderComponent.Model(
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                recommendedLocale = AppLocale("English", "en"),
                selectedLocale = AppLocale("English", "en"),
                availableLocales = emptyList(),
                localePickerAvailable = true,
                localePickerDisplayed = false,
                statusVisibility = StatusVisibility.PUBLIC,
                statusVisibilityPickerDisplayed = false,
                sendButtonAvailable = true,
            ),
        )
    }
}

@Preview
@Composable
private fun EditorHeaderContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        EditorHeaderContent(
            model = EditorHeaderComponent.Model(
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                recommendedLocale = AppLocale("English", "en"),
                selectedLocale = AppLocale("English", "en"),
                availableLocales = emptyList(),
                localePickerAvailable = true,
                localePickerDisplayed = false,
                statusVisibility = StatusVisibility.PUBLIC,
                statusVisibilityPickerDisplayed = false,
                sendButtonAvailable = true,
            ),
        )
    }
}
