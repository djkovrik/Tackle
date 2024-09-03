package com.sedsoftware.tackle.compose.ui.auth.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.custom.LoadingDotsText
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.auth_failed_to_connect
import tackle.shared.compose.generated.resources.auth_retry
import tackle.shared.compose.generated.resources.common_retry
import tackle.shared.compose.generated.resources.common_retrying

@Composable
internal fun ScreenFailedToConnect(
    isRetrying: Boolean,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.weight(weight = 0.5f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.auth_retry),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier.size(size = 128.dp),
                )

                Text(
                    text = stringResource(resource = Res.string.auth_failed_to_connect),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Right,
                    modifier = modifier.padding(all = 32.dp),
                )
            }
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.weight(weight = 0.5f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier,
            ) {
                AnimatedVisibility(
                    visible = isRetrying,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it },
                ) {
                    LoadingDotsText(
                        text = stringResource(resource = Res.string.common_retrying),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = modifier
                    )
                }

                TackleButton(
                    text = stringResource(resource = Res.string.common_retry),
                    enabled = !isRetrying,
                    onClick = onRetryClick,
                    modifier = modifier
                        .padding(all = 16.dp)
                        .navigationBarsPadding(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScreenFailedToConnectPreviewLight() {
    TackleScreenPreview {
        ScreenFailedToConnect(isRetrying = false)
    }
}

@Preview
@Composable
private fun ScreenFailedToConnectPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        ScreenFailedToConnect(isRetrying = false)
    }
}
