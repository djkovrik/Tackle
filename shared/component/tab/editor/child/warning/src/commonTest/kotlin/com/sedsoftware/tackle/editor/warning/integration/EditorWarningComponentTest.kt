package com.sedsoftware.tackle.editor.warning.integration

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorWarningComponentTest : ComponentTest<EditorWarningComponentDefault>() {

    private val activeModel: EditorWarningComponent.Model
        get() = component.model.value

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    override fun createComponent(): EditorWarningComponentDefault =
        EditorWarningComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            dispatchers = testDispatchers,
        )

    @Test
    fun `onTextInput should update model text`() = runTest {
        // given
        val text = "test text"
        // when
        component.onTextInput(text)
        // then
        assertThat(activeModel.text).isEqualTo(text)
    }

    @Test
    fun `clearTextInput should clear model text`() = runTest {
        // given
        val text = "test text"
        // when
        component.onTextInput(text)
        component.clearTextInput()
        // then
        assertThat(activeModel.text).isEmpty()
    }
}
