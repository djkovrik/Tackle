package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.editor.integration.EditorTabComponentPreview
import com.sedsoftware.tackle.explore.integration.ExploreTabComponentPreview
import com.sedsoftware.tackle.home.integration.HomeTabComponentPreview
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.integration.MainComponentPreview
import com.sedsoftware.tackle.notifications.integration.NotificationsTabComponentPreview
import com.sedsoftware.tackle.publications.integration.PublicationsTabComponentPreview

@Composable
internal fun BottomNavigationBar(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.childStack.subscribeAsState()
    val activeComponent: MainComponent.Child = stack.active.instance

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                )
            },
            label = { Text("Home") },
            selected = activeComponent is MainComponent.Child.TabHome,
            onClick = component::onHomeTabClicked,
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Explore",
                )
            },
            label = { Text("Explore") },
            selected = activeComponent is MainComponent.Child.TabExplore,
            onClick = component::onExploreTabClicked,
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Editor",
                )
            },
            label = { Text("Editor") },
            selected = activeComponent is MainComponent.Child.TabEditor,
            onClick = component::onEditorTabClicked,
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Publications",
                )
            },
            label = { Text("Publications") },
            selected = activeComponent is MainComponent.Child.TabPublications,
            onClick = component::onPublicationsTabClicked,
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                )
            },
            label = { Text("Notifications") },
            selected = activeComponent is MainComponent.Child.TabNotifications,
            onClick = component::onNotificationsTabClicked,
            alwaysShowLabel = false,
        )
    }
}


@Composable
@Preview
private fun BottomNavigationBarHomePreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabHome(HomeTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarExplorePreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabExplore(ExploreTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarEditorPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabEditor(EditorTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarPublicationsPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabPublications(PublicationsTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarNotificationsPreviewLight() {
    TackleScreenPreview {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabNotifications(NotificationsTabComponentPreview())
            )
        )
    }
}


@Composable
@Preview
private fun BottomNavigationBarHomePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabHome(HomeTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarExplorePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabExplore(ExploreTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarEditorPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabEditor(EditorTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarPublicationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabPublications(PublicationsTabComponentPreview())
            )
        )
    }
}

@Composable
@Preview
private fun BottomNavigationBarNotificationsPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        BottomNavigationBar(
            component = MainComponentPreview(
                child = MainComponent.Child.TabNotifications(NotificationsTabComponentPreview())
            )
        )
    }
}
