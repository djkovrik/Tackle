package com.sedsoftware.tackle.status.integration

import com.sedsoftware.tackle.status.StatusComponent.Model
import com.sedsoftware.tackle.status.store.StatusStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        status = it.status,
        menuVisible = it.menuVisible,
        menuActions = it.menuActions,
        translation = it.translation,
        translationInProgress = it.translationInProgress,
        translationDisplayed = it.translationDisplayed,
    )
}
