package com.sedsoftware.tackle.editor.emojis.store

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.emojis.domain.EditorEmojisManager
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisApiStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisDatabaseStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisSettingsStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorEmojisStoreTest : StoreTest<EditorEmojisStore.Intent, EditorEmojisStore.State, EditorEmojisStore.Label>() {

    private val api: EditorEmojisApiStub = EditorEmojisApiStub()
    private val db: EditorEmojisDatabaseStub = EditorEmojisDatabaseStub()
    private val settings: EditorEmojisSettingsStub = EditorEmojisSettingsStub()

    private val today: LocalDate
        get() = _today

    private var _today: LocalDate = LocalDate.parse("2024-01-01")

    private val manager: EditorEmojisManager = EditorEmojisManager(
        api = api,
        database = db,
        settings = settings,
        today = today,
    )

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `store creation should observe emojis`() = runTest {
        // given
        settings.lastCachedTimestamp = ""
        api.getServerEmojisResponse = EditorEmojisApiStub.correctResponse
        _today = LocalDate.parse("2024-01-01")
        // when
        store.init()
        // then
        assertThat(store.state.emojisAvailable).isTrue()
        assertThat(store.state.emojis).isNotEmpty()
    }

    @Test
    fun `ToggleComponentVisibility should change visibility state`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorEmojisStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.emojisVisible).isTrue()
        // when
        store.accept(EditorEmojisStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.emojisVisible).isFalse()
    }

    @Test
    fun `ResetState should reset store state`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorEmojisStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.emojisVisible).isTrue()
        // when
        store.accept(EditorEmojisStore.Intent.ResetState)
        // then
        assertThat(store.state.emojisVisible).isFalse()
    }

    override fun createStore(): Store<EditorEmojisStore.Intent, EditorEmojisStore.State, EditorEmojisStore.Label> =
        EditorEmojisStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
