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
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderSettingsStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorHeaderComponentTest : ComponentTest<EditorHeaderComponent>() {

    private val activeModel: EditorHeaderComponent.Model
        get() = component.model.value

    private val settings: EditorHeaderSettingsStub = EditorHeaderSettingsStub()

    private val tools: EditorHeaderComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
        every { getAvailableLocales() } returns listOf(AppLocale("English", "en"), AppLocale("Russian", "ru"))
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
    fun `onLocalePickerRequest should change locale picker visibility`() = runTest {
        // given
        // when
        component.onLocalePickerRequest(true)
        // then
        assertThat(activeModel.localePickerDisplayed).isTrue()
        // and when
        component.onLocalePickerRequest(false)
        // then
        assertThat(activeModel.localePickerDisplayed).isFalse()
    }

    @Test
    fun `onLocaleSelect should update selected locale`() = runTest {
        // given
        val targetLocale = AppLocale("Russian", "ru")
        // when
        component.onLocaleSelect(targetLocale)
        // then
        assertThat(activeModel.selectedLocale).isEqualTo(targetLocale)
    }

    @Test
    fun `onStatusVisibilityPickerRequest should change locale picker visibility`() = runTest {
        // given
        // when
        component.onStatusVisibilityPickerRequest(true)
        // then
        assertThat(activeModel.statusVisibilityPickerDisplayed).isTrue()
        // and when
        component.onStatusVisibilityPickerRequest(false)
        // then
        assertThat(activeModel.statusVisibilityPickerDisplayed).isFalse()
    }

    @Test
    fun `OnStatusVisibilitySelect should update visibility`() = runTest {
        // given
        // when
        component.onStatusVisibilitySelect(StatusVisibility.PUBLIC)
        // then
        assertThat(activeModel.statusVisibility).isEqualTo(StatusVisibility.PUBLIC)
    }

    @Test
    fun `changeSendingAvailability should update send button state`() = runTest {
        // given
        // when
        component.changeSendingAvailability(true)
        // then
        assertThat(activeModel.sendButtonAvailable).isTrue()
        // and when
        component.changeSendingAvailability(false)
        // then
        assertThat(activeModel.sendButtonAvailable).isFalse()
    }

    override fun createComponent(): EditorHeaderComponent =
        EditorHeaderComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            settings = settings,
            tools = tools,
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
        )
}
