package com.sedsoftware.tackle.compose.ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.home.HomeComponent
import com.sedsoftware.tackle.home.integration.HomeComponentPreview

@Composable
internal fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {
    val model: HomeComponent.Model by component.model.subscribeAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = model.text,
            modifier = modifier,
        )
    }
}

@Composable
@Preview
private fun PreviewHomeContent() {
    TackleScreenPreview {
        HomeContent(
            component = HomeComponentPreview()
        )
    }
}
