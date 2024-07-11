package com.sedsoftware.tackle.editor.warning.store

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorWarningStoreTest : StoreTest<EditorWarningStore.Intent, EditorWarningStore.State, Nothing>() {

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

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

    override fun createStore(): Store<EditorWarningStore.Intent, EditorWarningStore.State, Nothing> =
        EditorWarningStoreProvider(
            storeFactory = DefaultStoreFactory(),
            mainContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
