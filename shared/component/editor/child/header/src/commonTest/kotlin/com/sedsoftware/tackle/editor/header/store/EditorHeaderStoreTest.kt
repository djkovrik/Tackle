package com.sedsoftware.tackle.editor.header.store

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.editor.header.domain.EditorHeaderManager
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderSettingsStub
import com.sedsoftware.tackle.utils.test.StoreTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorHeaderStoreTest : StoreTest<EditorHeaderStore.Intent, EditorHeaderStore.State, EditorHeaderStore.Label>() {

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    private val settings: EditorHeaderSettingsStub = EditorHeaderSettingsStub()

    private val tools: EditorHeaderComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
        every { getAvailableLocales() } returns listOf(AppLocale("English", "en"), AppLocale("Russian", "ru"))
    }

    private val manager: EditorHeaderManager = EditorHeaderManager(settings, tools)

    @Test
    fun `store creation should fetch all required data`() = runTest {
        // given
        val avatar = "avatar"
        val domain = "domain"
        val nickname = "nickname"
        settings.avatar = avatar
        settings.domain = domain
        settings.nickname = nickname
        // when
        store.init()
        // then
        assertThat(store.state.avatar).isEqualTo(avatar)
        assertThat(store.state.domain).isEqualTo(domain)
        assertThat(store.state.nickname).isEqualTo(nickname)
        assertThat(store.state.recommendedLocale.languageName).isNotEmpty()
        assertThat(store.state.recommendedLocale.languageCode).isNotEmpty()
        assertThat(store.state.selectedLocale.languageName).isNotEmpty()
        assertThat(store.state.selectedLocale.languageCode).isNotEmpty()
        assertThat(store.state.availableLocales).isNotEmpty()
        assertThat(store.state.localePickerAvailable).isTrue()
    }

    @Test
    fun `store creation error on fetch should show error message`() = runTest {
        // given
        val avatar = "avatar"
        val domain = "domain"
        val nickname = "nickname"
        settings.avatar = avatar
        settings.domain = domain
        settings.nickname = nickname
        // when
        every { tools.getCurrentLocale() } throws IllegalStateException("Test")
        every { tools.getAvailableLocales() } throws IllegalStateException("Test")
        store.init()
        // then
        assertThat(labels.count { it is EditorHeaderStore.Label.ErrorCaught }).isGreaterThan(0)
    }

    @Test
    fun `OnLocalePickerRequested should change locale picker visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorHeaderStore.Intent.OnLocalePickerRequested(true))
        // then
        assertThat(store.state.localePickerDisplayed).isTrue()
        // and when
        store.accept(EditorHeaderStore.Intent.OnLocalePickerRequested(false))
        // then
        assertThat(store.state.localePickerDisplayed).isFalse()
    }

    @Test
    fun `OnLocaleSelected should update selected locale`() = runTest {
        // given
        val targetLocale = AppLocale("Russian", "ru")
        // when
        store.init()
        store.accept(EditorHeaderStore.Intent.OnLocaleSelected(targetLocale))
        // then
        assertThat(store.state.selectedLocale).isEqualTo(targetLocale)
    }

    @Test
    fun `OnLocaleSelected error should show error message`() = runTest {
        // given
        val targetLocale = AppLocale("Russian", "ru")
        // when
        every { tools.getCurrentLocale() } throws IllegalStateException("Test")
        every { tools.getAvailableLocales() } throws IllegalStateException("Test")
        store.init()
        store.accept(EditorHeaderStore.Intent.OnLocaleSelected(targetLocale))
        // then
        assertThat(labels.count { it is EditorHeaderStore.Label.ErrorCaught }).isGreaterThan(0)
    }

    @Test
    fun `OnVisibilityPickerRequested should change locale picker visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerRequested(true))
        // then
        assertThat(store.state.statusVisibilityPickerDisplayed).isTrue()
        // and when
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerRequested(false))
        // then
        assertThat(store.state.statusVisibilityPickerDisplayed).isFalse()
    }

    @Test
    fun `OnVisibilityPickerSelected should update visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerSelected(StatusVisibility.PUBLIC))
        // then
        assertThat(store.state.statusVisibility).isEqualTo(StatusVisibility.PUBLIC)
        // and when
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerSelected(StatusVisibility.DIRECT))
        // then
        assertThat(store.state.statusVisibility).isEqualTo(StatusVisibility.DIRECT)
        // and when
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerSelected(StatusVisibility.PRIVATE))
        // then
        assertThat(store.state.statusVisibility).isEqualTo(StatusVisibility.PRIVATE)
        // and when
        store.accept(EditorHeaderStore.Intent.OnVisibilityPickerSelected(StatusVisibility.UNLISTED))
        // then
        assertThat(store.state.statusVisibility).isEqualTo(StatusVisibility.UNLISTED)
    }

    @Test
    fun `ChangeSendingAvailability should change sending state`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorHeaderStore.Intent.ChangeSendingAvailability(true))
        // then
        assertThat(store.state.sendingAvailable).isTrue()
        // and when
        store.accept(EditorHeaderStore.Intent.ChangeSendingAvailability(false))
        // then
        assertThat(store.state.sendingAvailable).isFalse()
    }

    override fun createStore(): Store<EditorHeaderStore.Intent, EditorHeaderStore.State, EditorHeaderStore.Label> =
        EditorHeaderStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
