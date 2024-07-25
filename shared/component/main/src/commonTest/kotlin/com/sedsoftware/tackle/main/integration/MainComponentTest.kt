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
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainComponentTest : ComponentTest<MainComponent>() {

    private val unauthorizedApi: UnauthorizedApi = mock<UnauthorizedApi>()
    private val authorizedApi: AuthorizedApi = mock<AuthorizedApi>()
    private val database: TackleDatabase = mock<TackleDatabase>()
    private val settings: TackleSettings = mock<TackleSettings>()
    private val platformTools: TacklePlatformTools = mock<TacklePlatformTools>()

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `component creation should initialize stack with TabEditor`() {
        // given
        // when
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabEditor::class)
    }

    @Test
    fun `click to HOME tab should switch to TabHome`() {
        // given
        // when
        component.onTabClicked(TackleNavigationTab.HOME)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabHome::class)
    }

    @Test
    fun `click to EXPLORE tab should switch to TabExplore`() {
        // given
        // when
        component.onTabClicked(TackleNavigationTab.EXPLORE)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabExplore::class)
    }

    @Test
    fun `click to EDITOR tab should switch to TabEditor`() {
        // given
        // when
        component.onTabClicked(TackleNavigationTab.EDITOR)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabEditor::class)
    }

    @Test
    fun `click to PUBLICATIONS tab should switch to TabPublications`() {
        // given
        // when
        component.onTabClicked(TackleNavigationTab.PUBLICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabPublications::class)
    }

    @Test
    fun `click to NOTIFICATIONS tab should switch to TabNotifications`() {
        // given
        // when
        component.onTabClicked(TackleNavigationTab.NOTIFICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabNotifications::class)
    }

    override fun createComponent(): MainComponent =
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
