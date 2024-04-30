package com.sedsoftware.tackle.compose.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentPreview
import com.sedsoftware.tackle.compose.core.BackHandler
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.auth.part.LearnMoreBottomSheet
import com.sedsoftware.tackle.compose.ui.auth.part.ServerInfo
import com.sedsoftware.tackle.compose.widget.CustomOutlinedTextField
import com.sedsoftware.tackle.compose.widget.LoadingDotsText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.auth_app_label
import tackle.shared.compose.generated.resources.auth_default_server
import tackle.shared.compose.generated.resources.auth_learn_more
import tackle.shared.compose.generated.resources.auth_logo
import tackle.shared.compose.generated.resources.auth_server_address
import tackle.shared.compose.generated.resources.auth_server_address_wrong
import tackle.shared.compose.generated.resources.auth_server_info_fetch
import tackle.shared.compose.generated.resources.auth_server_recommended
import tackle.shared.compose.generated.resources.common_continue

@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    val model: AuthComponent.Model by component.model.subscribeAsState()
    val focusManager: FocusManager = LocalFocusManager.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val defaultServer: String = stringResource(Res.string.auth_default_server)

    BackHandler(backHandler = component.backHandler, isEnabled = model.isLearnMoreVisible) {
        component.onHideLearnMore()
    }

    val clearFocus = remember { { focusManager.clearFocus() } }
    val hideKeyboard = remember { { keyboardController?.hide() } }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .weight(weight = 0.5f)
        ) {
            Text(
                text = stringResource(Res.string.auth_app_label),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier.padding(all = 32.dp),
            )

            Image(
                painter = painterResource(Res.drawable.auth_logo),
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
                CustomOutlinedTextField(
                    value = model.textInput,
                    onValueChange = component::onTextInput,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    shape = MaterialTheme.shapes.large,
                    maxLines = 1,
                    singleLine = true,
                    isError = model.isServerInfoError,
                    label = {
                        Text(
                            text = if (!model.isServerInfoError) {
                                stringResource(Res.string.auth_server_address)
                            } else {
                                stringResource(Res.string.auth_server_address_wrong)
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
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
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
                        text = stringResource(Res.string.auth_server_recommended),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Left,
                        modifier = modifier
                            .padding(all = 8.dp)
                            .weight(weight = 0.5f)
                            .clickable(onClick = { component.onTextInput(defaultServer) }),
                    )

                    Text(
                        text = stringResource(Res.string.auth_learn_more),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Right,
                        modifier = modifier
                            .padding(all = 8.dp)
                            .weight(weight = 0.5f)
                            .clickable(onClick = { component.onShowLearnMore() }),
                    )
                }
            }

            AnimatedVisibility(visible = model.isLoadingServerInfo) {
                LoadingDotsText(
                    text = stringResource(Res.string.auth_server_info_fetch),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = modifier
                )
            }

            AnimatedVisibility(visible = model.isServerInfoLoaded) {
                hideKeyboard.invoke()
                clearFocus.invoke()

                ServerInfo(
                    name = model.serverName,
                    description = model.serverDescription,
                    modifier = modifier
                        .width(width = inputAndInfoWidth)
                        .padding(vertical = 8.dp),
                )
            }

            Spacer(modifier = modifier.weight(weight = 1f))

            Button(
                enabled = model.isServerInfoLoaded,
                shape = MaterialTheme.shapes.large,
                onClick = component::onAuthenticateClick,
                modifier = modifier.padding(all = 16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.common_continue),
                    modifier = modifier,
                )
            }
        }
    }

    if (model.isLearnMoreVisible) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { component.onHideLearnMore() },
            sheetState = bottomSheetState,
        ) {
            LearnMoreBottomSheet(
                onJoinMastodonClick = component::onJoinMastodonClick,
                modifier = modifier,
            )
        }
    }
}

private val inputAndInfoWidth = 320.dp

@Composable
@Preview
private fun PreviewAuthContentIdleLight() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}

@Composable
@Preview
private fun PreviewAuthContentIdleDark() {
    TackleScreenPreview(darkTheme = true) {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}

@Composable
@Preview
private fun PreviewAuthContentWithServerLight() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
            )
        )
    }
}

@Composable
@Preview
private fun PreviewAuthContentWithServerDark() {
    TackleScreenPreview(darkTheme = true) {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
            )
        )
    }
}
