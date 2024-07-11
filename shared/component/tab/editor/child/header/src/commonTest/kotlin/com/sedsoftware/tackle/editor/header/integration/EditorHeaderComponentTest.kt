package com.sedsoftware.tackle.editor.header.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderSettingsStub
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderToolsStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorHeaderComponentTest : ComponentTest<EditorHeaderComponentDefault>() {

    private val activeModel: EditorHeaderComponent.Model
        get() = component.model.value

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `onLocalePickerRequested should change locale picker visibility`() = runTest {
        // given
        // when
        component.onLocalePickerRequested(true)
        // then
        assertThat(activeModel.localePickerDisplayed).isTrue()
        // and when
        component.onLocalePickerRequested(false)
        // then
        assertThat(activeModel.localePickerDisplayed).isFalse()
    }

    @Test
    fun `onLocaleSelected should update selected locale`() = runTest {
        // given
        val targetLocale = AppLocale("Russian", "ru")
        // when
        component.onLocaleSelected(targetLocale)
        // then
        assertThat(activeModel.selectedLocale).isEqualTo(targetLocale)
    }

    @Test
    fun `onStatusVisibilityPickerRequested should change locale picker visibility`() = runTest {
        // given
        // when
        component.onStatusVisibilityPickerRequested(true)
        // then
        assertThat(activeModel.statusVisibilityPickerDisplayed).isTrue()
        // and when
        component.onStatusVisibilityPickerRequested(false)
        // then
        assertThat(activeModel.statusVisibilityPickerDisplayed).isFalse()
    }

    @Test
    fun `OnStatusVisibilitySelected should update visibility`() = runTest {
        // given
        // when
        component.onStatusVisibilitySelected(StatusVisibility.PUBLIC)
        // then
        assertThat(activeModel.statusVisibility).isEqualTo(StatusVisibility.PUBLIC)
    }

    override fun createComponent(): EditorHeaderComponentDefault =
        EditorHeaderComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            settings = EditorHeaderSettingsStub(),
            tools = EditorHeaderToolsStub(),
            dispatchers = testDispatchers,
            output = {},
        )
}
