package com.sedsoftware.tackle.compose.ui.auth

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.model.CredentialsInfoState
import com.sedsoftware.tackle.compose.core.BackHandler
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenAuthorize
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenCredentialsFound
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenFailedToConnect
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenLearnMore
import com.sedsoftware.tackle.compose.ui.auth.content.ScreenSplash

@Composable
internal fun AuthContent(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    val model: AuthComponent.Model by component.model.subscribeAsState()
    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BackHandler(backHandler = component.backHandler, isEnabled = model.isLearnMoreVisible) {
        component.onHideLearnMore()
    }

    when (model.credentialsState) {
        CredentialsInfoState.NOT_CHECKED -> {
            ScreenSplash()
        }

        CredentialsInfoState.UNAUTHORIZED -> {
            ScreenAuthorize(
                model = model,
                component = component,
            )
        }

        CredentialsInfoState.EXISTING_USER_CHECK_FAILED,
        CredentialsInfoState.RETRYING -> {
            ScreenFailedToConnect(
                isRetrying = model.credentialsState == CredentialsInfoState.RETRYING,
                onRetryClick = component::onRetryButtonClick,
            )
        }

        CredentialsInfoState.AUTHORIZED -> {
            ScreenCredentialsFound()
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
