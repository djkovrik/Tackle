package com.sedsoftware.tackle.editor.emojis.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisApiStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisDatabaseStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisSettingsStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorEmojisComponentTest : ComponentTest<EditorEmojisComponentDefault>() {

    private var componentOutput: ComponentOutput? = null

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `onEmojiClicked should use output`() = runTest {
        // given
        val emoji = CustomEmoji("a", "b", "c", true, "d")
        // when
        component.onEmojiClicked(emoji)
        // then
        assertThat(componentOutput).isEqualTo(ComponentOutput.StatusEditor.EmojiSelected(emoji))
    }

    override fun createComponent(): EditorEmojisComponentDefault = EditorEmojisComponentDefault(
        componentContext = DefaultComponentContext(lifecycle),
        storeFactory = DefaultStoreFactory(),
        api = EditorEmojisApiStub(),
        database = EditorEmojisDatabaseStub(),
        settings = EditorEmojisSettingsStub(),
        dispatchers = testDispatchers,
        output = { componentOutput = it },
    )
}
