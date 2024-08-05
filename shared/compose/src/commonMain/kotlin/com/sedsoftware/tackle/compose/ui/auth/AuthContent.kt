package com.sedsoftware.tackle.compose.ui.auth

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentPreview
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.compose.core.BackHandler
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenAuthorize
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenFailedToConnect
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenGenericSplash
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenLearnMore

@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier,
) {
    val model: AuthComponent.Model by component.model.subscribeAsState()
    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BackHandler(backHandler = component.backHandler, isEnabled = model.isLearnMoreVisible) {
        component.onHideLearnMore()
    }

    when (model.credentialsState) {
        CredentialsState.UNAUTHORIZED -> {
            ScreenAuthorize(
                model = model,
                onTextInput = component::onTextInput,
                onLearnMoreClick = component::onShowLearnMore,
                onAuthenticateClick = component::onAuthenticateClick,
            )
        }

        CredentialsState.EXISTING_USER_CHECK_FAILED,
        CredentialsState.RETRYING -> {
            ScreenFailedToConnect(
                isRetrying = model.credentialsState == CredentialsState.RETRYING,
                onRetryClick = component::onRetryButtonClick,
            )
        }

        CredentialsState.NOT_CHECKED,
        CredentialsState.AUTHORIZED -> {
            ScreenGenericSplash()
        }
    }

    if (model.isLearnMoreVisible) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { component.onHideLearnMore() },
            sheetState = bottomSheetState,
        ) {
            ScreenLearnMore(
                onJoinMastodonClick = component::onJoinMastodonClick,
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAuthContentIdleLight() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}

@Preview
@Composable
private fun PreviewAuthContentIdleDark() {
    TackleScreenPreview(darkTheme = true) {
        AuthContent(
            component = AuthComponentPreview()
        )
    }
}

@Preview
@Composable
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

@Preview
@Composable
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

@Preview
@Composable
private fun PreviewAuthContentCredentialsFailedLight() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
                credentialsState = CredentialsState.EXISTING_USER_CHECK_FAILED,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewAuthContentCredentialsFailedDark() {
    TackleScreenPreview(darkTheme = true) {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
                credentialsState = CredentialsState.EXISTING_USER_CHECK_FAILED,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewAuthContentCredentialsRetryingLight() {
    TackleScreenPreview {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
                credentialsState = CredentialsState.RETRYING,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewAuthContentCredentialsRetryingDark() {
    TackleScreenPreview(darkTheme = true) {
        AuthContent(
            component = AuthComponentPreview(
                textInput = "mastodon.social",
                isServerInfoLoaded = true,
                credentialsState = CredentialsState.RETRYING,
            )
        )
    }
}
