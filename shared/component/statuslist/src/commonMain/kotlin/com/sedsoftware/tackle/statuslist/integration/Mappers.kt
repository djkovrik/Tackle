package com.sedsoftware.tackle.statuslist.integration

import com.sedsoftware.tackle.statuslist.StatusListComponent.Model
import com.sedsoftware.tackle.statuslist.store.StatusListStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        initialProgressVisible = it.initialProgressVisible,
        loadMoreProgressVisible = it.loadMoreProgressVisible,
        emptyPlaceholderVisible = it.emptyPlaceholderVisible,
    )
}
