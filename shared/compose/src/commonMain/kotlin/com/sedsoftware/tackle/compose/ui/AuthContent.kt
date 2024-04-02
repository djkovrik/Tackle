package com.sedsoftware.tackle.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentPreview
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Auth component")
    }
}

@Composable
@Preview
internal fun AuthContentPreviewIdle() {
    TackleScreenPreview(darkTheme = false) {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}
