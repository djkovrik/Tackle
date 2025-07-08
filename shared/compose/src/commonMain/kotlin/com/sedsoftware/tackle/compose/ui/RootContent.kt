package com.sedsoftware.tackle.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.sedsoftware.tackle.compose.core.exceptionToString
import com.sedsoftware.tackle.compose.ui.alternatetext.AlternateTextContent
import com.sedsoftware.tackle.compose.ui.auth.AuthContent
import com.sedsoftware.tackle.compose.ui.editor.EditorContent
import com.sedsoftware.tackle.compose.ui.main.MainContent
import com.sedsoftware.tackle.compose.ui.media.ViewImageContent
import com.sedsoftware.tackle.compose.ui.media.ViewVideoContent
import com.sedsoftware.tackle.compose.widget.TackleSnackbar
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val alternateTextDialog: ChildSlot<*, AlternateTextComponent> by component.alternateTextDialog.subscribeAsState()
    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold { paddingValues: PaddingValues ->

        LaunchedEffect(component) {
            component.errorMessages.collect { exception ->
                exceptionToString(exception).takeIf { it.isNotEmpty() }?.let { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = fade() + scale()),
                modifier = modifier.fillMaxSize(),
            ) {
                when (val child = it.instance) {
                    is Child.Auth -> AuthContent(component = child.component)
                    is Child.Main -> MainContent(component = child.component)
                    is Child.Editor -> EditorContent(component = child.component)
                    is Child.ViewImage -> ViewImageContent(component = child.component)
                    is Child.ViewVideo -> ViewVideoContent(component = child.component)
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(paddingValues = paddingValues)
                    .padding(all = 16.dp),
            ) { snackbarData: SnackbarData ->
                TackleSnackbar(
                    message = snackbarData.visuals.message,
                    modifier = modifier,
                )
            }
        }
    }

    alternateTextDialog.child?.instance?.also { component: AlternateTextComponent ->
        val alternateTextModel: AlternateTextComponent.Model by component.model.subscribeAsState()

        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = component::onDismiss,
            sheetState = bottomSheetState,
        ) {
            AlternateTextContent(text = alternateTextModel.text)
        }

        LaunchedEffect(Unit) {
            bottomSheetState.show()
        }
    }
}
