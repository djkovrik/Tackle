package com.sedsoftware.tackle.compose.ui.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.EditorTabContent
import com.sedsoftware.tackle.compose.ui.explore.ExploreTabContent
import com.sedsoftware.tackle.compose.ui.home.HomeTabContent
import com.sedsoftware.tackle.compose.ui.notifications.NotificationsTabContent
import com.sedsoftware.tackle.compose.ui.publications.PublicationsTabContent
import com.sedsoftware.tackle.compose.widget.BottomNavigationBar
import com.sedsoftware.tackle.editor.integration.EditorTabComponentPreview
import com.sedsoftware.tackle.explore.integration.ExploreTabComponentPreview
import com.sedsoftware.tackle.home.integration.HomeTabComponentPreview
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.integration.MainComponentPreview
import com.sedsoftware.tackle.notifications.integration.NotificationsTabComponentPreview
import com.sedsoftware.tackle.publications.integration.PublicationsTabComponentPreview

@Composable
internal fun MainContent(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ChildrenContent(
            component = component,
            modifier = Modifier
                .weight(weight = 1f)
                .consumeWindowInsets(insets = WindowInsets.navigationBars)
        )

        BottomNavigationBar(
            component = component,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ChildrenContent(
    component: MainComponent,
    modifier: Modifier = Modifier
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is MainComponent.Child.TabHome -> HomeTabContent(component = child.component)
            is MainComponent.Child.TabExplore -> ExploreTabContent(component = child.component)
            is MainComponent.Child.TabEditor -> EditorTabContent(component = child.component)
            is MainComponent.Child.TabPublications -> PublicationsTabContent(component = child.component)
            is MainComponent.Child.TabNotifications -> NotificationsTabContent(component = child.component)
        }
    }
}

@Composable
@Preview
private fun MainContentHomePreviewLight() {
    TackleScreenPreview {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabHome(HomeTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentExplorePreviewLight() {
    TackleScreenPreview {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabExplore(ExploreTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentEditorPreviewLight() {
    TackleScreenPreview {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabEditor(EditorTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentPublicationsPreviewLight() {
    TackleScreenPreview {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabPublications(PublicationsTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentNotificationsPreviewLight() {
    TackleScreenPreview {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabNotifications(NotificationsTabComponentPreview())
            )
        )
    }
}


@Composable
@Preview
private fun MainContentHomePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabHome(HomeTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentExplorePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabExplore(ExploreTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentEditorPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabEditor(EditorTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentPublicationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabPublications(PublicationsTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun MainContentNotificationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        MainContent(
            component = MainComponentPreview(
                child = MainComponent.Child.TabNotifications(NotificationsTabComponentPreview())
            )
        )
    }
}
