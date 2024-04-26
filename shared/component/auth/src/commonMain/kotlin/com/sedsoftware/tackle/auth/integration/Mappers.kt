package com.sedsoftware.tackle.auth.integration

import com.sedsoftware.tackle.auth.AuthComponent.Model
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        textInput = it.userInput,
        serverName = it.instanceInfo.name,
        serverDescription = it.instanceInfo.description,
        serverUsers = it.instanceInfo.users,
        isLoadingServerInfo = it.instanceInfoState == InstanceInfoState.LOADING,
        isServerInfoLoaded = it.instanceInfoState == InstanceInfoState.LOADED,
        isServerInfoError = it.instanceInfoState == InstanceInfoState.ERROR,
        isOauthFlowActive = it.awaitingForOauth,
        isLearnMoreVisible = it.learnMoreVisible,
    )
}
