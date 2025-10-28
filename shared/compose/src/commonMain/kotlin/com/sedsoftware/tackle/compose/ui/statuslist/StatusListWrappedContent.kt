package com.sedsoftware.tackle.compose.ui.statuslist

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.TackleScreenDefaults
import com.sedsoftware.tackle.compose.TackleScreenTemplate
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_back

@Composable
internal fun StatusWrappedListContent(
    root: RootComponent,
    component: StatusListComponent,
    modifier: Modifier = Modifier,
) {
    val model: StatusListComponent.Model by component.model.subscribeAsState()

    TackleScreenTemplate(
        titleString = "#${(model.timeline as Timeline.HashTag).hashtag}",
        navigationIcon = Res.drawable.editor_back,
        onNavigationClick = root::onBack,
        colors = TackleScreenDefaults.colors(
            headerContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            headerContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier,
    ) {
        StatusListContent(
            component = component,
        )
    }
}
