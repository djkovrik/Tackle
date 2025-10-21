package com.sedsoftware.tackle.main.viewmedia.integration

import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent.Model
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        attachments = it.attachments,
        selectedIndex = it.selectedIndex,
        downloadInProgress = it.downloadInProgress,
        downloadProgress = it.downloadProgress,
        downloadCompleted = it.downloadCompleted,
    )
}
