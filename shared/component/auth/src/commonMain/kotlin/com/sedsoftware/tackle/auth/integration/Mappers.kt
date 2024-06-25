package com.sedsoftware.tackle.auth.integration

import com.sedsoftware.tackle.auth.AuthComponent.Model
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        textInput = it.userInput,
        serverName = it.instanceInfo.title,
        serverDescription = it.instanceInfo.description,
        serverUsers = it.instanceInfo.activePerMonth,
        credentialsState = it.credentialsState,
        isLoadingServerInfo = it.instanceInfoState == InstanceInfoState.LOADING,
        isServerInfoLoaded = it.instanceInfoState == InstanceInfoState.LOADED,
        isServerInfoError = it.instanceInfoState == InstanceInfoState.ERROR,
        isOauthFlowActive = it.oauthFlowActive,
        isLearnMoreVisible = it.learnMoreVisible,
    )
}
