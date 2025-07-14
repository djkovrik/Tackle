package com.sedsoftware.tackle.main.integration

import com.sedsoftware.tackle.domain.model.type.StatusVisibility.PUBLIC
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.UNLISTED
import com.sedsoftware.tackle.main.StatusComponent.Model
import com.sedsoftware.tackle.main.store.StatusStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        status = it.baseStatus.reblog.takeIf { it != null } ?: it.baseStatus,
        rebloggedBy = when {
            it.baseStatus.reblog != null && it.baseStatus.account.displayName.isNotEmpty() -> it.baseStatus.account.displayName
            it.baseStatus.reblog != null -> it.baseStatus.account.username
            else -> ""
        },
        reblogAvailable = it.baseStatus.visibility == PUBLIC || it.baseStatus.visibility == UNLISTED,
        extendedInfo = it.extendedInfo,
        isOwn = it.isOwn,
        menuVisible = it.menuVisible,
        menuActions = it.menuActions,
        translation = it.translation,
        translationInProgress = it.translationInProgress,
        translationDisplayed = it.translationDisplayed,
        hideSensitiveContent = it.hideSensitiveContent,
    )
}
