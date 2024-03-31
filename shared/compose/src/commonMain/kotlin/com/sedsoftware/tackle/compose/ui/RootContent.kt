package com.sedsoftware.tackle.compose.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.sedsoftware.tackle.compose.theme.TackleTheme
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    TackleTheme {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .windowInsetsPadding(
                        insets = WindowInsets.systemBars.only(
                            sides = WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                        )
                    )
            ) { paddingValues ->
                Children(
                    stack = component.childStack,
                    animation = stackAnimation(animator = fade() + scale()),
                    modifier = modifier.padding(paddingValues = paddingValues)
                ) {
                    when (val child = it.instance) {
                        is Child.Auth -> AuthContent(component = child.component)
                    }
                }
            }
        }
    }
}
