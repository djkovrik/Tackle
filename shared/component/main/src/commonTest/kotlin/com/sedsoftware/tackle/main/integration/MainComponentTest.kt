package com.sedsoftware.tackle.main.integration

import assertk.assertThat
import assertk.assertions.hasClass
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainComponentTest : ComponentTest<MainComponentDefault>() {

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
        assertThat(component.childStack.active.instance).hasClass(MainComponent.Child.TabHome::class)
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
            mainComponentOutput = {}
        )
}
