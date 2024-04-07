package com.sedsoftware.tackle.auth.integration

import com.sedsoftware.tackle.auth.AuthComponent.Model
import com.sedsoftware.tackle.auth.store.AuthStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        textInput = it.userInput,
        isRetrievingServerInfo = it.loadingServerInfo,
        serverPreviewVisible = it.serverInfo.name.isNotEmpty(),
        serverName = it.serverInfo.name,
        serverDescription = it.serverInfo.description,
        serverUsers = it.serverInfo.users,
        isOauthFlowActive = it.awaitingForOauth,
        isAuthenticated = it.authenticated,
        isLearnMoreVisible = it.learnMoreVisible,
    )
}
