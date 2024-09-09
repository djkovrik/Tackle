package com.sedsoftware.tackle.editor.warning.store

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class EditorWarningStoreTest : StoreTest<EditorWarningStore.Intent, EditorWarningStore.State, Nothing>() {

    @Test
    fun `OnTextInput should update state`() = runTest {
        // given
        val text = "test text"
        store.init()
        // when
        store.accept(EditorWarningStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.text).isEqualTo(text)
    }

    @Test
    fun `ToggleComponentVisibility should toggle content visibility`() = runTest {
        // given
        store.init()
        // when
        store.accept(EditorWarningStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.warningVisible).isTrue()
        // when
        store.accept(EditorWarningStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.warningVisible).isFalse()
    }

    override fun createStore(): Store<EditorWarningStore.Intent, EditorWarningStore.State, Nothing> =
        EditorWarningStoreProvider(
            storeFactory = DefaultStoreFactory(),
            mainContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
