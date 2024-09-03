package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun TackleSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    isRtl: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
) {
    Snackbar(
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
private fun TackleSnackbarPreviewLight() {
    TackleScreenPreview {
        TackleSnackbarPreviewContent()
    }
}

@Preview
@Composable
private fun TackleSnackbarPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleSnackbarPreviewContent()
    }
}

@Composable
private fun TackleSnackbarPreviewContent() {
    Box {
        TackleSnackbar(
            message = "No internet connection",
            modifier = Modifier.padding(all = 16.dp)
        )
    }
}
