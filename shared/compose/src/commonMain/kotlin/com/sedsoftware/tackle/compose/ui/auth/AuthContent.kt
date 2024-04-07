package com.sedsoftware.tackle.compose.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentPreview
import com.sedsoftware.tackle.compose.BackHandler
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.base.CustomOutlinedTextField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.auth_app_label
import tackle.shared.compose.generated.resources.auth_default_server
import tackle.shared.compose.generated.resources.auth_learn_more
import tackle.shared.compose.generated.resources.auth_logo
import tackle.shared.compose.generated.resources.auth_select_server
import tackle.shared.compose.generated.resources.auth_server_suggested

@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    val model: AuthComponent.Model by component.model.subscribeAsState()
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val defaultServer = stringResource(Res.string.auth_default_server)

    BackHandler(backHandler = component.backHandler, isEnabled = model.isLearnMoreVisible) {
        component.onHideLearnMore()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(Res.string.auth_app_label),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier,
        )

        Image(
            painter = painterResource(Res.drawable.auth_logo),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier.size(size = 128.dp),
        )

        Text(
            text = stringResource(Res.string.auth_select_server),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )

        CustomOutlinedTextField(
            value = model.textInput,
            onValueChange = component::onTextInput,
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = MaterialTheme.shapes.large,
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            modifier = modifier
                .width(width = 220.dp)
                .height(height = 40.dp)
        )

        AnimatedVisibility(visible = model.serverPreviewVisible) {
            ServerInfo(
                name = model.serverName,
                description = model.serverDescription,
                modifier = modifier,
            )
        }

        Text(
            text = stringResource(Res.string.auth_server_suggested),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier,
        )

        SuggestionChip(
            label = {
                Text(
                    text = defaultServer,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            onClick = { component.onTextInput(defaultServer) },
            modifier = modifier,
        )

        Text(
            text = stringResource(Res.string.auth_learn_more),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier,
        )

    }

    if (model.isLearnMoreVisible) {
        ModalBottomSheet(
            onDismissRequest = { component.onHideLearnMore() },
            sheetState = bottomSheetState,
        ) {
            LearnMoreBottomSheet()
        }
    }
}

@Composable
@Preview
private fun PreviewAuthContentWithInput() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "Abracadabra",
                serverPreviewVisible = true,
            )
        )
    }
}
