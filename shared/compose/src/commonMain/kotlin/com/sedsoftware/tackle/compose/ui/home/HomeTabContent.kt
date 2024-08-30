package com.sedsoftware.tackle.compose.ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleButton
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentPreview

@Composable
internal fun HomeTabContent(
    component: HomeTabComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Home tab",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )

        TackleButton(
            text = "Editor",
            onClick = component::onNewPostClick,
            modifier = Modifier.padding(all = 8.dp)
        )
    }
}

@Preview
@Composable
private fun HomeTabContentPreviewLight() {
    TackleScreenPreview {
        HomeTabContent(
            component = HomeTabComponentPreview()
        )
    }
}

@Preview
@Composable
private fun HomeTabContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        HomeTabContent(
            component = HomeTabComponentPreview()
        )
    }
}
