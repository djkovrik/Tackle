package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.main.StatusComponent
import com.sedsoftware.tackle.main.StatusComponent.Model
import com.sedsoftware.tackle.main.StatusComponentGateways
import com.sedsoftware.tackle.main.domain.StatusManager
import com.sedsoftware.tackle.main.model.StatusAction
import com.sedsoftware.tackle.main.model.StatusContextAction
import com.sedsoftware.tackle.main.store.StatusStore
import com.sedsoftware.tackle.main.store.StatusStore.Label
import com.sedsoftware.tackle.main.store.StatusStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StatusComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: StatusComponentGateways.Api,
    private val tools: StatusComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
    private val status: Status,
    private val extendedInfo: Boolean,
    private val isOwn: Boolean,
) : StatusComponent, ComponentContext by componentContext {

    private val store: StatusStore =
        instanceKeeper.getStore {
            StatusStoreProvider(
                storeFactory = storeFactory,
                manager = StatusManager(api, tools),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
                status = status,
                extendedInfo = extendedInfo,
                isOwn = isOwn,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.StatusDeleted -> output(ComponentOutput.SingleStatus.Deleted(label.statusId))
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.exception))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onStatusAction(action: StatusAction) {
        when (action) {
            StatusAction.FAVOURITE -> store.accept(StatusStore.Intent.OnFavouriteClicked)
            StatusAction.REBLOG -> store.accept(StatusStore.Intent.OnReblogClicked)
            StatusAction.SHARE -> store.accept(StatusStore.Intent.OnShareClicked)
            StatusAction.REPLY -> output(ComponentOutput.SingleStatus.ReplyCalled(model.value.status.id))
        }
    }

    override fun onMenuActionClick(action: StatusContextAction) {
        when (action) {
            StatusContextAction.TRANSLATE -> store.accept(StatusStore.Intent.OnTranslateClicked)
            StatusContextAction.SHOW_ORIGINAL -> store.accept(StatusStore.Intent.OnShowOriginalClicked)
            StatusContextAction.DELETE -> store.accept(StatusStore.Intent.OnDeleteClicked)
            StatusContextAction.PIN -> store.accept(StatusStore.Intent.OnPinClicked)
            StatusContextAction.UNPIN -> store.accept(StatusStore.Intent.OnPinClicked)
            StatusContextAction.BOOKMARK -> store.accept(StatusStore.Intent.OnBookmarkClicked)
            StatusContextAction.UNBOOKMARK -> store.accept(StatusStore.Intent.OnBookmarkClicked)
            StatusContextAction.MUTE -> store.accept(StatusStore.Intent.OnMuteClicked)
            StatusContextAction.UNMUTE -> store.accept(StatusStore.Intent.OnMuteClicked)
        }
    }

    override fun onMenuRequest(visible: Boolean) {
        store.accept(StatusStore.Intent.OnMenuVisibilityChanged(visible))
    }

    override fun onPollSelect(index: Int, multiselect: Boolean) {
        store.accept(StatusStore.Intent.OnPollOptionSelected(index, multiselect))
    }

    override fun onVoteClick() {
        store.accept(StatusStore.Intent.OnVoteClicked)
    }

    override fun onUrlClick(url: String) {
        store.accept(StatusStore.Intent.OnUrlClicked(url))
    }

    override fun onHashTagClick(hashTag: String) {
        output(ComponentOutput.SingleStatus.HashTagClicked(hashTag))
    }

    override fun onMentionClick(mention: String) {
        output(ComponentOutput.SingleStatus.MentionClicked(mention))
    }

    override fun onAlternateTextRequest(text: String) {
        output(ComponentOutput.SingleStatus.AlternateTextClicked(text))
    }
}
