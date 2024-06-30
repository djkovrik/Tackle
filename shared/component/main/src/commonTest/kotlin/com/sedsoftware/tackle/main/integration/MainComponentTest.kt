package com.sedsoftware.tackle.main.integration

import assertk.assertThat
import assertk.assertions.hasClass
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Token
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.flow.Flow
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainComponentTest : ComponentTest<MainComponentDefault>() {

    // Stubs for component creation, functionality tested inside related modules
    // TODO replace with some kind of mock library? Too verbose now
    private val unauthorizedApi: UnauthorizedApi = object : UnauthorizedApi {
        override suspend fun getServerInfo(url: String): Instance = TODO()
        override suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application = TODO()
        override suspend fun obtainToken(id: String, secret: String, code: String, uri: String, scopes: String): Token = TODO()
        override suspend fun getServerEmojis(): List<CustomEmoji> = TODO()
    }

    private val authorizedApi: AuthorizedApi = object : AuthorizedApi {
        override suspend fun verifyCredentials(): Account = TODO()
    }

    private val database: TackleDatabase = object : TackleDatabase {
        override suspend fun insertEmojis(list: List<CustomEmoji>) = TODO()
        override suspend fun observeEmojis(): Flow<List<CustomEmoji>> = TODO()
    }

    private val settings: TackleSettings = object : TackleSettings {
        var stub: String = ""
        override var domain: String = stub
        override var clientId: String = stub
        override var clientSecret: String = stub
        override var token: String = stub
        override var ownAvatar: String = stub
        override var ownUsername: String = stub
        override var emojiLastCachedTimestamp: String = stub
        override var lastSelectedLanguageName: String = stub
        override var lastSelectedLanguageCode: String = stub
    }

    private val platformTools: TacklePlatformTools = object : TacklePlatformTools {
        override fun openUrl(url: String?) = TODO()
        override fun getClientData(): AppClientData = TODO()
        override fun getCurrentLocale(): AppLocale = TODO()
        override fun getAvailableLocales(): List<AppLocale> = TODO()
    }

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `component creation should initialize stack with Home`() {
        // given
        // when
        component = createComponent()
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabEditor::class)
    }

    @Test
    fun `click to HOME tab should switch to TabHome`() {
        // given
        // when
        component = createComponent()
        component.onTabClicked(TackleNavigationTab.HOME)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabHome::class)
    }

    @Test
    fun `click to EXPLORE tab should switch to TabHome`() {
        // given
        // when
        component = createComponent()
        component.onTabClicked(TackleNavigationTab.EXPLORE)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabExplore::class)
    }

    @Test
    fun `click to EDITOR tab should switch to TabEditor`() {
        // given
        // when
        component = createComponent()
        component.onTabClicked(TackleNavigationTab.EDITOR)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabEditor::class)
    }

    @Test
    fun `click to PUBLICATIONS tab should switch to TabEditor`() {
        // given
        // when
        component = createComponent()
        component.onTabClicked(TackleNavigationTab.PUBLICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabPublications::class)
    }

    @Test
    fun `click to NOTIFICATIONS tab should switch to TabNotifications`() {
        // given
        // when
        component = createComponent()
        component.onTabClicked(TackleNavigationTab.NOTIFICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabNotifications::class)
    }

    override fun createComponent(): MainComponentDefault =
        MainComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            unauthorizedApi = unauthorizedApi,
            authorizedApi = authorizedApi,
            database = database,
            settings = settings,
            platformTools = platformTools,
            dispatchers = testDispatchers,
            mainComponentOutput = {}
        )
}
