package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun CustomSnackBar(
    message: String,
    isRtl: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onError,
    containerColor: Color = MaterialTheme.colorScheme.error,
    modifier: Modifier = Modifier,
) {
    Snackbar(
        containerColor = containerColor,
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Text(
                text = message,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
private fun CustomSnackBarPreview() {
    TackleScreenPreview {
        CustomSnackBar(
            message = "No internet connection"
        )
    }
}
