package com.sedsoftware.tackle.compose.ui.editor.header

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.header.content.ActionBarIcon
import com.sedsoftware.tackle.compose.ui.editor.header.content.ActionBarIconWithText
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_back
import tackle.shared.compose.generated.resources.editor_language
import tackle.shared.compose.generated.resources.editor_send
import tackle.shared.compose.generated.resources.main_tab_editor

@Composable
internal fun EditorHeaderContent(
    model: EditorHeaderComponent.Model,
    modifier: Modifier = Modifier,
    backIconVisible: Boolean = false,
    onLocalePickerRequest: () -> Unit = {},
    onVisibilityPickerRequest: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 176.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {
        Column {
            Row {
                if (backIconVisible) {
                    ActionBarIcon(
                        iconRes = Res.drawable.editor_back,
                        onClick = onBackClick,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.width(width = 16.dp))
                }

                Text(
                    text = stringResource(Res.string.main_tab_editor),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                ActionBarIconWithText(
                    text = model.selectedLocale.languageCode,
                    iconRes = Res.drawable.editor_language,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onLocalePickerRequest,
                )

                Spacer(modifier = Modifier.width(width = 16.dp))

                ActionBarIcon(
                    iconRes = Res.drawable.editor_send,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onSendClick,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(size = 48.dp)
                        .clip(shape = CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                ) {
                    Image(
                        painter = rememberImagePainter(url = model.avatar),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = CircleShape)
                    )
                }

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

                ActionBarIconWithText(
                    text = stringResource(model.statusVisibility.getTitle()),
                    iconRes = model.statusVisibility.getIcon(),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onVisibilityPickerRequest,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditorHeaderContentPreview() {
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
            ),
        )
    }
}

@Preview
@Composable
private fun EditorHeaderContentPreviewWithBack() {
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
            ),
            backIconVisible = true
        )
    }
}
