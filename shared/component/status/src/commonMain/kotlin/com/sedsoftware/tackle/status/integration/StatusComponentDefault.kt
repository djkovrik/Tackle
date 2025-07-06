package com.sedsoftware.tackle.status.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponent.Model
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.alternatetext.AlternateTextComponent
import com.sedsoftware.tackle.status.alternatetext.integration.AlternateTextComponentDefault
import com.sedsoftware.tackle.status.domain.StatusManager
import com.sedsoftware.tackle.status.model.StatusAction
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.status.store.StatusStore
import com.sedsoftware.tackle.status.store.StatusStore.Label
import com.sedsoftware.tackle.status.store.StatusStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

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

    private val alternateTextNavigation: SlotNavigation<AlternateTextConfig> = SlotNavigation<AlternateTextConfig>()

    override val alternateTextDialog: Value<ChildSlot<*, AlternateTextComponent>> =
        childSlot(
            source = alternateTextNavigation,
            serializer = AlternateTextConfig.serializer(),
            handleBackButton = true,
        ) { config, childComponentContext ->
            AlternateTextComponentDefault(
                componentContext = childComponentContext,
                text = config.text,
                onDismissed = alternateTextNavigation::dismiss,
            )
        }

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
        alternateTextNavigation.activate(AlternateTextConfig(text = text))
    }

    @Serializable
    private data class AlternateTextConfig(
        val text: String,
    )
}
