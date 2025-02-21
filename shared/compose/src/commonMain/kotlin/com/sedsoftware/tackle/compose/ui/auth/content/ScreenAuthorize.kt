package com.sedsoftware.tackle.compose.ui.auth.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.compose.custom.LoadingDotsText
import com.sedsoftware.tackle.compose.widget.TackleButton
import com.sedsoftware.tackle.compose.widget.TackleTextField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.app_label
import tackle.shared.compose.generated.resources.auth_learn_more
import tackle.shared.compose.generated.resources.auth_logo
import tackle.shared.compose.generated.resources.auth_server_address
import tackle.shared.compose.generated.resources.auth_server_address_wrong
import tackle.shared.compose.generated.resources.auth_server_info_fetch
import tackle.shared.compose.generated.resources.auth_server_recommended
import tackle.shared.compose.generated.resources.common_continue
import tackle.shared.compose.generated.resources.common_redirecting
import tackle.shared.compose.generated.resources.default_server

@Composable
internal fun ScreenAuthorize(
    model: AuthComponent.Model,
    modifier: Modifier = Modifier,
    onTextInput: (String) -> Unit = {},
    onLearnMoreClick: () -> Unit = {},
    onAuthenticateClick: () -> Unit = {},
) {
    val focusManager: FocusManager = LocalFocusManager.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val clearFocus = remember { { focusManager.clearFocus() } }
    val hideKeyboard = remember { { keyboardController?.hide() } }
    val defaultServer: String = stringResource(resource = Res.string.default_server)

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .weight(weight = 0.5f)
        ) {
            Text(
                text = stringResource(resource = Res.string.app_label),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier.padding(all = 32.dp),
            )

            Image(
                painter = painterResource(resource = Res.drawable.auth_logo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = modifier.size(size = 128.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .weight(weight = 0.5f)
        ) {
            Column(modifier = modifier.width(width = inputAndInfoWidth)) {
                TackleTextField(
                    value = model.textInput,
                    onValueChange = onTextInput,
                    singleLine = true,
                    isError = model.isServerInfoError,
                    label = {
                        Text(
                            text = if (!model.isServerInfoError) {
                                stringResource(resource = Res.string.auth_server_address)
                            } else {
                                stringResource(resource = Res.string.auth_server_address_wrong)
                            },
                            color = if (!model.isServerInfoError) {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            style = MaterialTheme.typography.labelMedium,
                            modifier = modifier
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(horizontal = 8.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            clearFocus.invoke()
                            hideKeyboard.invoke()
                        }
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .height(height = 46.dp)
                )

                Row(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(resource = Res.string.auth_server_recommended),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Left,
                        modifier = modifier
                            .padding(all = 8.dp)
                            .weight(weight = 0.5f)
                            .clickable(onClick = { onTextInput.invoke(defaultServer) }),
                    )

                    Text(
                        text = stringResource(resource = Res.string.auth_learn_more),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Right,
                        modifier = modifier
                            .padding(all = 8.dp)
                            .weight(weight = 0.5f)
                            .clickable(onClick = onLearnMoreClick),
                    )
                }
            }

            AnimatedVisibility(visible = model.isLoadingServerInfo) {
                LoadingDotsText(
                    text = stringResource(resource = Res.string.auth_server_info_fetch),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = modifier,
                )
            }

            AnimatedVisibility(visible = model.isServerInfoLoaded) {
                if (model.isServerInfoLoaded) {
                    hideKeyboard.invoke()
                    clearFocus.invoke()
                }

                ServerInfo(
                    name = model.serverName,
                    description = model.serverDescription,
                    modifier = modifier
                        .width(width = inputAndInfoWidth)
                        .padding(vertical = 8.dp),
                )
            }

            Spacer(modifier = modifier.weight(weight = 1f))

            AnimatedVisibility(
                visible = model.isOauthFlowActive,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
            ) {
                LoadingDotsText(
                    text = stringResource(resource = Res.string.common_redirecting),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = modifier,
                )
            }

            TackleButton(
                text = stringResource(resource = Res.string.common_continue),
                enabled = model.isServerInfoLoaded && !model.isOauthFlowActive,
                onClick = onAuthenticateClick,
                modifier = modifier
                    .padding(all = 16.dp)
                    .navigationBarsPadding(),
            )
        }
    }
}

private val inputAndInfoWidth = 320.dp
