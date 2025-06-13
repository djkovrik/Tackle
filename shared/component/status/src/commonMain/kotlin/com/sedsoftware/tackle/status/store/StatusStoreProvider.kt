package com.sedsoftware.tackle.status.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.domain.StatusManager
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.status.store.StatusStore.Intent
import com.sedsoftware.tackle.status.store.StatusStore.Label
import com.sedsoftware.tackle.status.store.StatusStore.State
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class StatusStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: StatusManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val status: Status,
    private val rebloggedBy: String,
    private val extendedInfo: Boolean,
    private val isOwn: Boolean,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): StatusStore =
        object : StatusStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "StatusStore",
            initialState = State(status = status, extendedInfo = extendedInfo, isOwn = isOwn, rebloggedBy = rebloggedBy),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.RefreshContextMenu)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.RefreshContextMenu> {
                    launch {
                        val status = state().status
                        val translated = state().translationDisplayed

                        unwrap(
                            result = withContext(ioContext) { manager.buildContextActions(status, isOwn, translated) },
                            onSuccess = { dispatch(Msg.MenuRefreshed(it)) },
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }

                onIntent<Intent.OnDeleteClicked> {
                    launch {
                        val statusId = state().status.id

                        unwrap(
                            result = withContext(ioContext) { manager.delete(statusId = statusId, deleteMedia = true) },
                            onSuccess = { publish(Label.StatusDeleted(statusId)) },
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }

                onIntent<Intent.OnTranslateClicked> {
                    launch {
                        val statusId = state().status.id
                        if (state().translation != null) {
                            dispatch(Msg.ShowTranslationRequested)
                        } else {
                            dispatch(Msg.TranslationRequested)

                            unwrap(
                                result = withContext(ioContext) { manager.translate(statusId) },
                                onSuccess = { translation: Translation ->
                                    dispatch(Msg.StatusTranslated(translation))
                                    forward(Action.RefreshContextMenu)
                                },
                                onError = { throwable: Throwable ->
                                    dispatch(Msg.TranslationFailed)
                                    publish(Label.ErrorCaught(throwable))
                                },
                            )
                        }
                    }
                }

                onIntent<Intent.OnShowOriginalClicked> {
                    dispatch(Msg.ShowOriginalRequested)
                }

                onIntent<Intent.OnFavouriteClicked> {
                    launch {
                        val statusId = state().status.id
                        val oldValue = state().status.favourited
                        val newValue = !oldValue

                        dispatch(Msg.StatusFavourited(newValue))

                        unwrap(
                            result = withContext(ioContext) { manager.favourite(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusFavourited(oldValue))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnReblogClicked> {
                    launch {
                        val statusId = state().status.id
                        val oldValue = state().status.reblogged
                        val newValue = !oldValue

                        dispatch(Msg.StatusReblogged(newValue))

                        unwrap(
                            result = withContext(ioContext) { manager.boost(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusReblogged(oldValue))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnBookmarkClicked> {
                    launch {
                        val statusId = state().status.id
                        val oldValue = state().status.bookmarked
                        val newValue = !oldValue

                        dispatch(Msg.StatusBookmarked(newValue))

                        unwrap(
                            result = withContext(ioContext) { manager.bookmark(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusBookmarked(oldValue))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnPinClicked> {
                    launch {
                        val statusId = state().status.id
                        val oldValue = state().status.pinned
                        val newValue = !oldValue

                        dispatch(Msg.StatusPinned(newValue))

                        unwrap(
                            result = withContext(ioContext) { manager.pin(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusPinned(oldValue))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnMuteClicked> {
                    launch {
                        val statusId = state().status.id
                        val oldValue = state().status.muted
                        val newValue = !oldValue

                        dispatch(Msg.StatusMuted(newValue))

                        unwrap(
                            result = withContext(ioContext) { manager.mute(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusMuted(oldValue))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnShareClicked> {
                    launch {
                        val title = status.account.displayName.takeIf { it.isNotEmpty() } ?: status.account.username
                        val url = status.url

                        unwrap(
                            result = manager.share(title, url),
                            onSuccess = {},
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }

                onIntent<Intent.OnMenuVisibilityChanged> {
                    dispatch(Msg.MenuVisibilityChanged(it.visible))
                }

                onIntent<Intent.OnPollOptionSelected> {
                    launch {
                        val currentVotes: List<Int> = state().status.poll?.ownVotes.orEmpty()
                        val newVotes = when {
                            it.multiselect && currentVotes.contains(it.index) -> currentVotes.filterNot { element -> element == it.index }
                            it.multiselect && !currentVotes.contains(it.index) -> (currentVotes + listOf(it.index)).sorted()
                            else -> listOf(it.index)
                        }

                        dispatch(Msg.PollOptionsUpdated(newVotes))
                    }
                }

                onIntent<Intent.OnVoteClicked> {
                    launch {
                        val currentVotes: List<Int> = state().status.poll?.ownVotes.orEmpty()
                        state().status.poll?.id?.let { pollId: String ->
                            unwrap(
                                result = withContext(ioContext) { manager.vote(pollId, currentVotes) },
                                onSuccess = { dispatch(Msg.PollVoted) },
                                onError = { publish(Label.ErrorCaught(it)) },
                            )
                        }
                    }
                }

                onIntent<Intent.OnUrlClicked> {
                    launch {
                        unwrap(
                            result = manager.openUrl(it.url),
                            onSuccess = {},
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.MenuRefreshed -> copy(
                        menuVisible = false,
                        menuActions = msg.actions,
                    )

                    is Msg.MenuVisibilityChanged -> copy(
                        menuVisible = msg.visible,
                    )

                    is Msg.TranslationRequested -> copy(
                        translation = null,
                        translationInProgress = true,
                        translationDisplayed = false,
                    )

                    is Msg.StatusTranslated -> copy(
                        translation = msg.translation,
                        translationInProgress = false,
                        translationDisplayed = true,
                    )

                    is Msg.TranslationFailed -> copy(
                        translationInProgress = false,
                    )

                    is Msg.ShowTranslationRequested -> copy(
                        translationDisplayed = true,
                    )

                    is Msg.ShowOriginalRequested -> copy(
                        translationDisplayed = false,
                    )

                    is Msg.StatusFavourited -> copy(
                        status = status.copy(favourited = msg.favourited),
                    )

                    is Msg.StatusReblogged -> copy(
                        status = status.copy(reblogged = msg.reblogged),
                    )

                    is Msg.StatusBookmarked -> copy(
                        status = status.copy(bookmarked = msg.bookmarked),
                    )

                    is Msg.StatusPinned -> copy(
                        status = status.copy(pinned = msg.pinned),
                    )

                    is Msg.StatusMuted -> copy(
                        status = status.copy(muted = msg.muted),
                    )

                    is Msg.PollOptionsUpdated -> copy(
                        status = status.copy(
                            poll = status.poll?.copy(
                                ownVotes = msg.options,
                            ),
                        ),
                    )

                    is Msg.PollVoted -> copy(
                        status = status.copy(
                            poll = status.poll?.copy(
                                voted = true,
                            ),
                        ),
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object RefreshContextMenu : Action
    }

    private sealed interface Msg {
        data class MenuRefreshed(val actions: List<StatusContextAction>) : Msg
        data class MenuVisibilityChanged(val visible: Boolean) : Msg
        data object TranslationRequested : Msg
        data class StatusTranslated(val translation: Translation) : Msg
        data object TranslationFailed : Msg
        data object ShowTranslationRequested : Msg
        data object ShowOriginalRequested : Msg
        data class StatusFavourited(val favourited: Boolean) : Msg
        data class StatusReblogged(val reblogged: Boolean) : Msg
        data class StatusBookmarked(val bookmarked: Boolean) : Msg
        data class StatusPinned(val pinned: Boolean) : Msg
        data class StatusMuted(val muted: Boolean) : Msg
        data class PollOptionsUpdated(val options: List<Int>) : Msg
        data object PollVoted : Msg
    }
}
