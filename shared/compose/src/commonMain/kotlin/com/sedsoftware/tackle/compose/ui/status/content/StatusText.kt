package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import com.sedsoftware.tackle.compose.custom.LoadingDotsText
import com.sedsoftware.tackle.compose.widget.TackleStatusRichText
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.utils.extension.isValidUrl
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_retrying
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
    inlinedContent: @Composable (String) -> Unit = {
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
) {
    if (model.translation != null && model.translationDisplayed) {
        Column {
            Text(
                text = model.translation?.content.orEmpty(),
                color = textColor,
                style = style,
                modifier = modifier,
            )

            Text(
                text = "${stringResource(resource = Res.string.status_translated)} ${model.translation?.provider.orEmpty()}",
                color = textColor,
                style = style,
                modifier = modifier,
            )
        }
    } else {
        Column {
            AnimatedVisibility(
                visible = model.translationInProgress
            ) {
                LoadingDotsText(
                    text = stringResource(resource = Res.string.status_translating),
                    color = textColor,
                    style = style,
                    modifier = modifier,
                )
            }

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
        }
    }
}
