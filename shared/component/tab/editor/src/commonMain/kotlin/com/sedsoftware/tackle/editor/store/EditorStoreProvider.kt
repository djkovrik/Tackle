package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.editor.domain.StatusEditorManager
import com.sedsoftware.tackle.editor.model.EditorProfileData
import com.sedsoftware.tackle.editor.store.EditorStore.Intent
import com.sedsoftware.tackle.editor.store.EditorStore.Label
import com.sedsoftware.tackle.editor.store.EditorStore.State
import com.sedsoftware.tackle.network.model.CustomEmoji
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.model.AppLocale
import com.sedsoftware.tackle.utils.unwrap
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: StatusEditorManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorStore =
        object : EditorStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.FetchProfileData)
                dispatch(Action.FetchAvailableLocales)
                dispatch(Action.FetchServerEmojis)
                dispatch(Action.ObserveCachedEmojis)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.FetchProfileData> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getEditorProfileData() },
                            onSuccess = { profileData: EditorProfileData ->
                                Msg.ProfileDataLoaded(profileData)
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.FetchDeviceLocale> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getCurrentLocale() },
                            onSuccess = { currentLocale: AppLocale ->
                                Msg.SelectedLocaleUpdated(currentLocale)
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.FetchAvailableLocales> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getAvailableLocales() },
                            onSuccess = { availableLocales: List<AppLocale> ->
                                Msg.AvailableLocalesLoaded(availableLocales)
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.FetchServerEmojis> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.fetchServerEmojis() },
                            onSuccess = {},
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.ObserveCachedEmojis> {
                    launch(ioContext) {
                        manager.observeCachedEmojis()
                            .flowOn(mainContext)
                            .catch { throwable -> publish(Label.ErrorCaught(throwable)) }
                            .collect { dispatch(Msg.EmojiLoaded(it)) }
                    }
                }

                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.TextInput(it.text))
                }

                onIntent<Intent.OnShowEmojiPanel> {
                    dispatch(Msg.EmojiPanelVisibilityChanged(it.show))
                }

                onIntent<Intent.OnShowLanguagePicker> { }

                onIntent<Intent.OnLocaleSelected> {
                    dispatch(Msg.SelectedLocaleUpdated(it.language))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.TextInput -> copy(
                        textInput = msg.text,
                        symbolsLeft = MAX_SYMBOLS_LIMIT - msg.text.length,
                        symbolsLimitExceeded = msg.text.length <= MAX_SYMBOLS_LIMIT,
                    )

                    is Msg.ProfileDataLoaded -> copy(
                        ownAvatar = msg.data.avatar,
                        ownNickname = msg.data.name,
                    )

                    is Msg.SelectedLocaleUpdated -> copy(
                        selectedLocale = msg.locale,
                    )

                    is Msg.AvailableLocalesLoaded -> copy(
                        availableLocales = msg.locales,
                        localeSelectionAvailable = msg.locales.isNotEmpty(),
                    )

                    is Msg.EmojiLoaded -> copy(
                        emojis = msg.emojis,
                        emojisAvailable = msg.emojis.isNotEmpty(),
                    )

                    is Msg.EmojiPanelVisibilityChanged -> copy(
                        emojiPanelVisible = msg.visible,
                    )

                    is Msg.LocaleSelectionVisibilityChanged -> copy(
                        localePickerVisible = msg.visible,
                    )
                }
            },
        ) {}

    private sealed interface Action {
        data object FetchProfileData : Action
        data object FetchDeviceLocale : Action
        data object FetchAvailableLocales : Action
        data object FetchServerEmojis : Action
        data object ObserveCachedEmojis : Action
    }

    private sealed interface Msg {
        data class TextInput(val text: String) : Msg
        data class ProfileDataLoaded(val data: EditorProfileData) : Msg
        data class SelectedLocaleUpdated(val locale: AppLocale) : Msg
        data class AvailableLocalesLoaded(val locales: List<AppLocale>) : Msg
        data class EmojiLoaded(val emojis: List<CustomEmoji>) : Msg
        data class EmojiPanelVisibilityChanged(val visible: Boolean) : Msg
        data class LocaleSelectionVisibilityChanged(val visible: Boolean) : Msg
    }

    private companion object {
        const val MAX_SYMBOLS_LIMIT = 500
    }
}
