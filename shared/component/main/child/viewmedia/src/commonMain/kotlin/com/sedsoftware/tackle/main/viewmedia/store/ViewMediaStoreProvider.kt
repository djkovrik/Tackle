package com.sedsoftware.tackle.main.viewmedia.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.domain.FileDownloadManager
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.Intent
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.Label
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class ViewMediaStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: FileDownloadManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(
        attachments: List<MediaAttachment>,
        selectedIndex: Int,
        autoInit: Boolean = true,
    ): ViewMediaStore =
        object : ViewMediaStore, Store<Intent, State, Label> by storeFactory.create<Intent, Nothing, Msg, State, Label>(
            name = "ViewMediaStore",
            initialState = State(attachments = attachments, selectedIndex = selectedIndex),
            autoInit = autoInit,
            executorFactory = coroutineExecutorFactory(mainContext) {
                onIntent<Intent.OnSelectionChanged> {
                    dispatch(Msg.SelectionChanged(it.index))
                }

                onIntent<Intent.OnDownloadClicked> {
                    launch {
                        val index = state().selectedIndex
                        val url = state().attachments[index].url
                        manager.downloadFile(url = url, destination = it.destination)
                            .flowOn(ioContext)
                            .catch { throwable: Throwable ->
                                dispatch(Msg.DownloadFailed(index))
                                publish(Label.ErrorCaught(throwable))
                            }
                            .collect { progress: Float ->
                                dispatch(Msg.DownloadProgressChanged(index, progress))
                            }
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.DownloadProgressChanged -> copy(
                        downloadInProgress = downloadInProgress.update(msg.index, msg.progress > 0f && msg.progress < 1f),
                        downloadProgress = downloadProgress.update(msg.index, msg.progress),
                        downloadCompleted = downloadCompleted.update(msg.index, msg.progress == 1f),
                    )

                    is Msg.DownloadFailed -> copy(
                        downloadInProgress = downloadInProgress.update(msg.index, false),
                        downloadProgress = downloadProgress.update(msg.index, 0f),
                        downloadCompleted = downloadCompleted.update(msg.index, false),
                    )

                    is Msg.SelectionChanged -> copy(
                        selectedIndex = msg.index,
                    )
                }

            }
        ) {}

    sealed interface Msg {
        data class DownloadProgressChanged(val index: Int, val progress: Float) : Msg
        data class DownloadFailed(val index: Int) : Msg
        data class SelectionChanged(val index: Int) : Msg
    }

    private fun <T> List<T>.update(at: Int, value: T): List<T> =
        mapIndexed { index: Int, item: T -> if (index == at) value else item }
}
