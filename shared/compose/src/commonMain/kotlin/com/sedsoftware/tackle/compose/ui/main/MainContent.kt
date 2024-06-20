package com.sedsoftware.tackle.compose.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.main.MainComponent

@Composable
internal fun MainContent(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ChildrenContent(
            component = component,
            modifier = Modifier
                .weight(1f)
                .consumeWindowInsets(WindowInsets.navigationBars)
        )

        BottomNavigationBar(
            component = component,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChildrenContent(component: MainComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is MainComponent.Child.TabHome ->
                DummyTabContent(text = "Home", modifier = Modifier.background(color = Color.Red.copy(alpha = 0.1f)))

            is MainComponent.Child.TabBrowse ->
                DummyTabContent(text = "Browse", modifier = Modifier.background(color = Color.Green.copy(alpha = 0.1f)))

            is MainComponent.Child.TabEditor ->
                DummyTabContent(text = "Editor", modifier = Modifier.background(color = Color.Blue.copy(alpha = 0.1f)))

            is MainComponent.Child.TabFeeds ->
                DummyTabContent(text = "Feeds", modifier = Modifier.background(color = Color.Cyan.copy(alpha = 0.1f)))

            is MainComponent.Child.TabNotifications ->
                DummyTabContent(text = "Notification", modifier = Modifier.background(color = Color.Magenta.copy(alpha = 0.1f)))
        }
    }
}


@Composable
private fun BottomNavigationBar(
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
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Browse",
                )
            },
            label = { Text("Browse") },
            selected = activeComponent is MainComponent.Child.TabBrowse,
            onClick = component::onBrowseTabClicked,
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
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Feeds",
                )
            },
            label = { Text("Feeds") },
            selected = activeComponent is MainComponent.Child.TabFeeds,
            onClick = component::onFeedsTabClicked,
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
        )
    }
}

@Composable
private fun DummyTabContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
