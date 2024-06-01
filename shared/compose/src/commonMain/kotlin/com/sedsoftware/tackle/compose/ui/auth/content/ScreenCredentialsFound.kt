package com.sedsoftware.tackle.compose.ui.auth.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_redirecting

@Composable
internal fun ScreenCredentialsFound(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(Res.string.common_redirecting),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun ScreenCredentialsFoundPreviewLight() {
    TackleScreenPreview {
        ScreenCredentialsFound()
    }
}

@Preview
@Composable
private fun ScreenCredentialsFoundPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        ScreenCredentialsFound()
    }
}
