package com.sedsoftware.tackle.compose.ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.TackleScreenDefaults
import com.sedsoftware.tackle.compose.TackleScreenTemplate
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleAppBarButton
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentPreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.home_new_post
import tackle.shared.compose.generated.resources.home_scheduled_posts
import tackle.shared.compose.generated.resources.main_tab_home

@Composable
internal fun HomeTabContent(
    component: HomeTabComponent,
    modifier: Modifier = Modifier,
) {
    TackleScreenTemplate(
        title = Res.string.main_tab_home,
        colors = TackleScreenDefaults.colors(
            headerContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            headerContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        actions = {
            TackleAppBarButton(
                iconRes = Res.drawable.home_scheduled_posts,
                contentDescriptionRes = Res.string.home_scheduled_posts,
                onClick = component::onScheduledPostsClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = component::onNewPostClick,
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.home_new_post),
                    contentDescription = stringResource(resource = Res.string.home_new_post),
                    modifier = Modifier.size(size = 24.dp),
                )
            }
        },
        modifier = modifier.consumeWindowInsets(insets = WindowInsets.navigationBars),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

        }
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
