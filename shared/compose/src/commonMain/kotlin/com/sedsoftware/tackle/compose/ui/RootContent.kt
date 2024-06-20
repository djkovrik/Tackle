package com.sedsoftware.tackle.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.sedsoftware.tackle.compose.theme.TackleTheme
import com.sedsoftware.tackle.compose.ui.auth.AuthContent
import com.sedsoftware.tackle.compose.ui.main.MainContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    TackleTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(insets = WindowInsets.safeDrawing)
        ) {
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = fade() + scale()),
                modifier = modifier.fillMaxSize(),
            ) {
                when (val child = it.instance) {
                    is Child.Auth -> AuthContent(component = child.component)
                    is Child.Main -> MainContent(component = child.component)
                }
            }
        }
    }
}
