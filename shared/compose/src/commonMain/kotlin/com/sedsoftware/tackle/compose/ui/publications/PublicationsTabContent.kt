package com.sedsoftware.tackle.compose.ui.publications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.TackleScreenDefaults
import com.sedsoftware.tackle.compose.TackleScreenTemplate
import com.sedsoftware.tackle.compose.custom.navigation.TopNavigationBar
import com.sedsoftware.tackle.compose.model.TopNavigationTab
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.status.StatusPreviewStubs
import com.sedsoftware.tackle.compose.ui.statuslist.StatusListContent
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.integration.PublicationsTabComponentPreview
import com.sedsoftware.tackle.publications.model.PublicationsScreenTab
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.main_tab_publications
import tackle.shared.compose.generated.resources.publications_tab_global
import tackle.shared.compose.generated.resources.publications_tab_local

@Composable
internal fun PublicationsTabContent(
    component: PublicationsTabComponent,
    modifier: Modifier = Modifier,
) {

    val stack by component.childStack.subscribeAsState()
    val activeComponent: PublicationsTabComponent.Child = stack.active.instance
    val activeTab: PublicationsScreenTab =
        if (activeComponent is PublicationsTabComponent.Child.LocalTimeline) {
            PublicationsScreenTab.LOCAL
        } else {
            PublicationsScreenTab.REMOTE
        }

    TackleScreenTemplate(
        title = Res.string.main_tab_publications,
        colors = TackleScreenDefaults.colors(
            headerContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            headerContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier.consumeWindowInsets(insets = WindowInsets.navigationBars),
    ) {
        Column(modifier = modifier) {
            TopNavigationBar(
                selectedTabIndex = activeTab.index,
                tabs = listOf(
                    TopNavigationTab(
                        textResource = Res.string.publications_tab_local,
                        onClick = component::onLocalTabClick,
                    ),
                    TopNavigationTab(
                        textResource = Res.string.publications_tab_global,
                        onClick = component::onRemoteTabClick,
                    )
                ),
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = modifier,
            )

            ChildrenContent(
                component = component,
                modifier = modifier.weight(weight = 1f),
            )
        }
    }
}

@Composable
private fun ChildrenContent(
    component: PublicationsTabComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(animator = fade()),
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is PublicationsTabComponent.Child.LocalTimeline -> StatusListContent(component = child.component)
            is PublicationsTabComponent.Child.RemoteTimeline -> StatusListContent(component = child.component)
        }
    }
}

@Preview
@Composable
private fun PublicationsTabContentPreviewLight() {
    TackleScreenPreview {
        PublicationsTabContent(
            component = PublicationsTabComponentPreview(
                statuses = listOf(
                    StatusPreviewStubs.status.copy(id = "1"),
                    StatusPreviewStubs.statusWithLongTexts.copy(id = "2", reblogged = true),
                    StatusPreviewStubs.statusWithPollVotedExpired.copy(id = "3"),
                )
            )
        )
    }
}

@Preview
@Composable
private fun PublicationsTabContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        PublicationsTabContent(
            component = PublicationsTabComponentPreview(
                statuses = listOf(
                    StatusPreviewStubs.status.copy(id = "1", favourited = true),
                    StatusPreviewStubs.statusWithLongTexts.copy(id = "2"),
                    StatusPreviewStubs.statusWithPollNotVoted.copy(id = "3"),
                )
            )
        )
    }
}
