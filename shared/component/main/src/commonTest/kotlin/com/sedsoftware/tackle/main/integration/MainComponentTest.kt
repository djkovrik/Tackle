package com.sedsoftware.tackle.main.integration

import assertk.assertThat
import assertk.assertions.hasClass
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainComponentTest : ComponentTest<MainComponent>() {

    private val authorizedApi: AuthorizedApi = mock<AuthorizedApi>()
    private val settings: TackleSettings = mock<TackleSettings>()
    private val platformTools: TacklePlatformTools = mock<TacklePlatformTools>()

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `component creation should initialize stack with TabEditor`() {
        // given
        // when
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.HomeTab::class)
    }

    @Test
    fun `click to HOME tab should switch to TabHome`() {
        // given
        // when
        component.onTabClick(TackleNavigationTab.HOME)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.HomeTab::class)
    }

    @Test
    fun `click to EXPLORE tab should switch to TabExplore`() {
        // given
        // when
        component.onTabClick(TackleNavigationTab.EXPLORE)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.ExploreTab::class)
    }

    @Test
    fun `click to PUBLICATIONS tab should switch to TabPublications`() {
        // given
        // when
        component.onTabClick(TackleNavigationTab.PUBLICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.PublicationsTab::class)
    }

    @Test
    fun `click to NOTIFICATIONS tab should switch to TabNotifications`() {
        // given
        // when
        component.onTabClick(TackleNavigationTab.NOTIFICATIONS)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.NotificationsTab::class)
    }

    @Test
    fun `click to PROFILE tab should switch to TabEditor`() {
        // given
        // when
        component.onTabClick(TackleNavigationTab.PROFILE)
        // then
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.ProfileTab::class)
    }

    override fun createComponent(): MainComponent =
        MainComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            authorizedApi = authorizedApi,
            settings = settings,
            platformTools = platformTools,
            dispatchers = testDispatchers,
            mainComponentOutput = {}
        )
}
