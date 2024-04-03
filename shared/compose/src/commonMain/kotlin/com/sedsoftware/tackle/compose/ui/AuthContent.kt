@file:OptIn(ExperimentalResourceApi::class)

package com.sedsoftware.tackle.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentPreview
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.auth_default_server
import tackle.shared.compose.generated.resources.auth_select_server
import tackle.shared.compose.generated.resources.auth_server_suggested
import tackle.shared.compose.generated.resources.common_next

// TODO Design
@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    val model: AuthComponent.Model by component.model.subscribeAsState()
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(Res.string.auth_select_server),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 32.dp),
        )

        OutlinedTextField(
            value = model.textInput,
            onValueChange = component::onTextInput,
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            modifier = modifier
        )

        Column(modifier = modifier.padding(all = 32.dp)) {
            Text(
                text = "Name: ${model.serverName}",
                style = MaterialTheme.typography.bodySmall,
            )

            Text(
                text = "Description: ${model.serverDescription}",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Text(
            text = stringResource(Res.string.auth_server_suggested),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier.padding(top = 16.dp),
        )

        val defaultServer = stringResource(Res.string.auth_default_server)
        SuggestionChip(
            label = {
                Text(
                    text = defaultServer,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            onClick = {
                component.onTextInput(defaultServer)
            }
        )


        Button(
            onClick = component::onAuthenticateClick,
            modifier = modifier.padding(all = 32.dp)
        ) {
            Text(
                text = stringResource(Res.string.common_next)
            )
        }
    }
}

@Composable
@Preview
internal fun AuthContentPreviewIdle() {
    TackleScreenPreview(darkTheme = false) {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}

@Composable
@Preview
internal fun AuthContentPreviewWithText() {
    TackleScreenPreview(darkTheme = false) {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social"
            )
        )
    }
}

