package com.sedsoftware.tackle.editor.header.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.domain.EditorHeaderManager
import com.sedsoftware.tackle.editor.header.model.EditorProfileData
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.Intent
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.Label
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.unwrap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorHeaderStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorHeaderManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorHeaderStore =
        object : EditorHeaderStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.FetchProfileData)
                dispatch(Action.FetchRecommendedLocale)
                dispatch(Action.FetchAvailableLocales)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.FetchProfileData> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getEditorProfileData() },
                            onSuccess = { profileData: EditorProfileData ->
                                dispatch(Msg.ProfileDataLoaded(profileData))
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.FetchRecommendedLocale> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getRecommendedLocale() },
                            onSuccess = { recommended: AppLocale ->
                                dispatch(Msg.RecommendedLocaleLoaded(recommended))
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
                                dispatch(Msg.AvailableLocalesLoaded(availableLocales))
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnShowLocalePicker> {
                    dispatch(Msg.LocaleDialogVisibilityChanged(it.show))
                }

                onIntent<Intent.OnLocaleSelected> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.saveSelectedLocale(it.language) },
                            onSuccess = {
                                dispatch(Msg.LocaleSelected(it))
                            },
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnShowStatusVisibilityPicker> {
                    dispatch(Msg.StatusDialogVisibilityChanged(it.show))
                }

                onIntent<Intent.OnStatusVisibilitySelected> {
                    dispatch(Msg.VisibilitySelected(it.visibility))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.ProfileDataLoaded -> copy(
                        avatar = msg.data.avatar,
                        nickname = msg.data.name,
                        domain = msg.data.domain,
                    )

                    is Msg.RecommendedLocaleLoaded -> copy(
                        recommendedLocale = msg.locale,
                        selectedLocale = msg.locale,
                    )

                    is Msg.LocaleSelected -> copy(
                        selectedLocale = msg.locale,
                    )

                    is Msg.AvailableLocalesLoaded -> copy(
                        availableLocales = msg.locales,
                        localePickerAvailable = msg.locales.isNotEmpty(),
                    )

                    is Msg.LocaleDialogVisibilityChanged -> copy(
                        localePickerDisplayed = msg.visible,
                    )

                    is Msg.StatusDialogVisibilityChanged -> copy(
                        statusVisibilityPickerDisplayed = msg.visible,
                    )

                    is Msg.VisibilitySelected -> copy(
                        statusVisibility = msg.visibility,
                    )
                }
            },
        ) {}

    private sealed interface Action {
        data object FetchProfileData : Action
        data object FetchRecommendedLocale : Action
        data object FetchAvailableLocales : Action
    }

    private sealed interface Msg {
        data class ProfileDataLoaded(val data: EditorProfileData) : Msg
        data class RecommendedLocaleLoaded(val locale: AppLocale) : Msg
        data class AvailableLocalesLoaded(val locales: List<AppLocale>) : Msg
        data class LocaleDialogVisibilityChanged(val visible: Boolean) : Msg
        data class LocaleSelected(val locale: AppLocale) : Msg
        data class StatusDialogVisibilityChanged(val visible: Boolean) : Msg
        data class VisibilitySelected(val visibility: StatusVisibility) : Msg
    }
}
