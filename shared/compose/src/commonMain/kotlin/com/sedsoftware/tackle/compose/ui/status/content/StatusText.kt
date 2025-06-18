package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.custom.LoadingDotsText
import com.sedsoftware.tackle.compose.widget.TackleStatusRichText
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.utils.extension.isValidUrl
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_translated
import tackle.shared.compose.generated.resources.status_translating

@Composable
internal fun StatusText(
    model: StatusComponent.Model,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
    onHashTagClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
    inlinedContent: @Composable (String) -> Unit = {},
) {
    if (model.translation != null && model.translationDisplayed) {
        Column {
            TackleStatusRichText(
                content = model.translation?.content.orEmpty(),
                emojis = model.status.emojis,
                modifier = modifier,
                style = style,
                textColor = textColor,
                highlightColor = highlightColor,
                onClick = { link: String ->
                    when {
                        link.startsWith("#") -> onHashTagClick.invoke(link)
                        link.startsWith("@") -> onMentionClick.invoke(link)
                        link.isValidUrl() -> onUrlClick.invoke(link)
                    }
                },
                inlinedContent = inlinedContent,
            )

            Text(
                text = "${stringResource(resource = Res.string.status_translated)} ${model.translation?.provider.orEmpty()}",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier.padding(top = 4.dp),
            )
        }
    } else {
        Column {
            TackleStatusRichText(
                content = model.status.contentAsPlainText,
                emojis = model.status.emojis,
                modifier = modifier,
                style = style,
                textColor = textColor,
                highlightColor = highlightColor,
                onClick = { link: String ->
                    when {
                        link.startsWith("#") -> onHashTagClick.invoke(link)
                        link.startsWith("@") -> onMentionClick.invoke(link)
                        link.isValidUrl() -> onUrlClick.invoke(link)
                    }
                },
                inlinedContent = inlinedContent,
            )

            AnimatedVisibility(
                visible = model.translationInProgress
            ) {
                LoadingDotsText(
                    text = stringResource(resource = Res.string.status_translating),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
