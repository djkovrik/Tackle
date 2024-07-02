package com.sedsoftware.tackle.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.sedsoftware.tackle.compose.core.exceptionToString
import com.sedsoftware.tackle.compose.ui.auth.AuthContent
import com.sedsoftware.tackle.compose.ui.main.MainContent
import com.sedsoftware.tackle.compose.widget.CustomSnackBar
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    Scaffold { paddingValues: PaddingValues ->

        LaunchedEffect(component) {
            component.errorMessages.collect { exception ->
                exceptionToString(exception).takeIf { it.isNotEmpty() }?.let { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(paddingValues = paddingValues)
                    .padding(all = 16.dp),
            ) { snackbarData: SnackbarData ->
                CustomSnackBar(
                    message = snackbarData.visuals.message,
                    modifier = modifier,
                )
            }

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
