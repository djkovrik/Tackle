package com.sedsoftware.tackle.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.main.domain.StatusManager
import com.sedsoftware.tackle.main.model.StatusContextAction
import com.sedsoftware.tackle.main.store.StatusStore.Intent
import com.sedsoftware.tackle.main.store.StatusStore.Label
import com.sedsoftware.tackle.main.store.StatusStore.State
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
    private val extendedInfo: Boolean,
    private val isOwn: Boolean,
) {
    @StoreCreate
    fun create(autoInit: Boolean = true): StatusStore =
        object : StatusStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "StatusStore_${status.id}",
            initialState = State(
                baseStatus = status,
                extendedInfo = extendedInfo,
                isOwn = isOwn,
            ),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.RefreshContextMenu)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.RefreshContextMenu> {
                    launch {
                        val translated = state().translationDisplayed

                        unwrap(
                            result = withContext(ioContext) { manager.buildContextActions(targetStatus, isOwn, translated) },
                            onSuccess = { dispatch(Msg.MenuRefreshed(it)) },
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }

                onIntent<Intent.OnDeleteClicked> {
                    launch {
                        val statusId = targetStatus.id
                        dispatch(Msg.MenuVisibilityChanged(false))

                        unwrap(
                            result = withContext(ioContext) { manager.delete(statusId = statusId, deleteMedia = true) },
                            onSuccess = { publish(Label.StatusDeleted(statusId)) },
                            onError = { publish(Label.ErrorCaught(it)) },
                        )
                    }
                }

                onIntent<Intent.OnTranslateClicked> {
                    launch {
                        dispatch(Msg.MenuVisibilityChanged(false))

                        val statusId = targetStatus.id
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
                    dispatch(Msg.MenuVisibilityChanged(false))
                    dispatch(Msg.ShowOriginalRequested)
                    forward(Action.RefreshContextMenu)
                }

                onIntent<Intent.OnFavouriteClicked> {
                    launch {
                        val statusId = targetStatus.id
                        val oldValue = targetStatus.favourited
                        val newValue = !oldValue
                        val oldCounter = targetStatus.favouritesCount
                        val newCounter = if (newValue) oldCounter + 1 else oldCounter - 1

                        dispatch(Msg.MenuVisibilityChanged(false))
                        dispatch(Msg.StatusFavourited(newValue, newCounter))

                        unwrap(
                            result = withContext(ioContext) { manager.favourite(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusFavourited(oldValue, oldCounter))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnReblogClicked> {
                    launch {
                        val statusId = targetStatus.id
                        val oldValue = targetStatus.reblogged
                        val newValue = !oldValue
                        val oldCounter = targetStatus.reblogsCount
                        val newCounter = if (newValue) oldCounter + 1 else oldCounter - 1

                        dispatch(Msg.StatusReblogged(newValue, newCounter))

                        unwrap(
                            result = withContext(ioContext) { manager.boost(statusId, newValue) },
                            onSuccess = { forward(Action.RefreshContextMenu) },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.StatusReblogged(oldValue, oldCounter))
                                forward(Action.RefreshContextMenu)
                            },
                        )
                    }
                }

                onIntent<Intent.OnBookmarkClicked> {
                    launch {
                        val statusId = targetStatus.id
                        val oldValue = targetStatus.bookmarked
                        val newValue = !oldValue

                        dispatch(Msg.MenuVisibilityChanged(false))
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
                        val statusId = targetStatus.id
                        val oldValue = targetStatus.pinned
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
                        val statusId = targetStatus.id
                        val oldValue = targetStatus.muted
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
                        val currentVotes: List<Int> = targetStatus.poll?.ownVotes.orEmpty()
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
                        val currentVotes: List<Int> = targetStatus.poll?.ownVotes.orEmpty()
                        targetStatus.poll?.id?.let { pollId: String ->
                            unwrap(
                                result = withContext(ioContext) { manager.vote(pollId, currentVotes) },
                                onSuccess = { poll: Poll ->
                                    dispatch(Msg.PollVoted(poll)) },
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
                        baseStatus = baseStatus.copy(
                            favourited = msg.favourited,
                            favouritesCount = msg.counter,
                            reblog = baseStatus.reblog?.copy(
                                favourited = msg.favourited,
                                favouritesCount = msg.counter,
                            )
                        ),
                    )

                    is Msg.StatusReblogged -> copy(
                        baseStatus = baseStatus.copy(
                            reblogged = msg.reblogged,
                            reblogsCount = msg.counter,
                            reblog = baseStatus.reblog?.copy(
                                reblogged = msg.reblogged,
                                reblogsCount = msg.counter,
                            )
                        ),
                    )

                    is Msg.StatusBookmarked -> copy(
                        baseStatus = baseStatus.copy(
                            bookmarked = msg.bookmarked,
                            reblog = baseStatus.reblog?.copy(
                                bookmarked = msg.bookmarked,
                            )
                        ),
                    )

                    is Msg.StatusPinned -> copy(
                        baseStatus = baseStatus.copy(
                            pinned = msg.pinned,
                            reblog = baseStatus.reblog?.copy(
                                pinned = msg.pinned,
                            )
                        ),
                    )

                    is Msg.StatusMuted -> copy(
                        baseStatus = baseStatus.copy(
                            muted = msg.muted,
                            reblog = baseStatus.reblog?.copy(
                                muted = msg.muted,
                            )
                        ),
                    )

                    is Msg.PollOptionsUpdated -> copy(
                        baseStatus = baseStatus.copy(
                            poll = baseStatus.poll?.copy(
                                ownVotes = msg.options,
                            ),
                            reblog = baseStatus.reblog?.copy(
                                poll = baseStatus.reblog?.poll?.copy(
                                    ownVotes = msg.options,
                                ),
                            )
                        ),
                    )

                    is Msg.PollVoted -> copy(
                        baseStatus = baseStatus.copy(
                            poll = if (baseStatus.poll != null) {
                                msg.updatedPoll
                            } else {
                                null
                            },
                            reblog = baseStatus.reblog?.copy(
                                poll = msg.updatedPoll,
                            )
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
        data class StatusFavourited(val favourited: Boolean, val counter: Int) : Msg
        data class StatusReblogged(val reblogged: Boolean, val counter: Int) : Msg
        data class StatusBookmarked(val bookmarked: Boolean) : Msg
        data class StatusPinned(val pinned: Boolean) : Msg
        data class StatusMuted(val muted: Boolean) : Msg
        data class PollOptionsUpdated(val options: List<Int>) : Msg
        data class PollVoted(val updatedPoll: Poll) : Msg
    }

    private val CoroutineExecutorScope<State, Msg, Action, Label>.targetStatus: Status
        get() = with(state()) { baseStatus.reblog.takeIf { it != null } ?: baseStatus }
}
