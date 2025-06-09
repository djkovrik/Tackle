package com.sedsoftware.tackle.editor.emojis.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import com.sedsoftware.tackle.editor.emojis.Responses
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisDatabaseStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisSettingsStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorEmojisComponentTest : ComponentTest<EditorEmojisComponent>() {

    private val activeModel: EditorEmojisComponent.Model
        get() = component.model.value

    private val api: EditorEmojisGateways.Api = mock {
        everySuspend { getServerEmojis() } returns Responses.correctResponse
    }

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onEmojiClick should use output`() = runTest {
        // given
        val emoji = CustomEmoji("a", "b", "c", true, "d")
        // when
        component.onEmojiClick(emoji)
        // then
        assertThat(componentOutput).contains(ComponentOutput.StatusEditor.EmojiSelected(emoji))
    }

    @Test
    fun `toggleComponentVisibility should update component visibility`() = runTest {
        // given
        // when
        component.toggleComponentVisibility()
        // then
        assertThat(activeModel.emojisContentVisible).isTrue()
        // when
        component.toggleComponentVisibility()
        // then
        assertThat(activeModel.emojisContentVisible).isFalse()
    }

    override fun createComponent(): EditorEmojisComponent =
        EditorEmojisComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            database = EditorEmojisDatabaseStub(),
            settings = EditorEmojisSettingsStub(),
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
        )
}
