package com.sedsoftware.tackle.status.integration

import com.sedsoftware.tackle.domain.model.type.StatusVisibility.PUBLIC
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.UNLISTED
import com.sedsoftware.tackle.status.StatusComponent.Model
import com.sedsoftware.tackle.status.store.StatusStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        status = it.status,
        rebloggedBy = it.rebloggedBy,
        reblogAvailable = it.status.visibility == PUBLIC || it.status.visibility == UNLISTED,
        extendedInfo = it.extendedInfo,
        isOwn = it.isOwn,
        menuVisible = it.menuVisible,
        menuActions = it.menuActions,
        translation = it.translation,
        translationInProgress = it.translationInProgress,
        translationDisplayed = it.translationDisplayed,
    )
}
