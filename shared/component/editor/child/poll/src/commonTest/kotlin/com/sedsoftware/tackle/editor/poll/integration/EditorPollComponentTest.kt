package com.sedsoftware.tackle.editor.poll.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.Instances
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorPollComponentTest : ComponentTest<EditorPollComponent>() {

    private val activeModel: EditorPollComponent.Model
        get() = component.model.value

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onDurationPickerRequest should show duration picker`() = runTest {
        // given
        // when
        component.onDurationPickerRequest(true)
        // then
        assertThat(activeModel.durationPickerVisible).isTrue()
        // and when
        component.onDurationPickerRequest(false)
        // then
        assertThat(activeModel.durationPickerVisible).isFalse()
    }

    @Test
    fun `onDurationSelect should set duration`() = runTest {
        // given
        val duration = PollDuration.THREE_DAYS
        // when
        component.onDurationSelect(duration)
        // then
        assertThat(activeModel.duration).isEqualTo(duration)
    }

    @Test
    fun `onMultiselectEnable should enable multiselect`() = runTest {
        // given
        // when
        component.onMultiselectEnable(true)
        // then
        assertThat(activeModel.multiselectEnabled).isTrue()
        // and when
        component.onMultiselectEnable(false)
        // then
        assertThat(activeModel.multiselectEnabled).isFalse()
    }

    @Test
    fun `onHideTotalsEnable should enable multiselect`() = runTest {
        // given
        // when
        component.onHideTotalsEnable(true)
        // then
        assertThat(activeModel.hideTotalsEnabled).isTrue()
        // and when
        component.onHideTotalsEnable(false)
        // then
        assertThat(activeModel.hideTotalsEnabled).isFalse()
    }

    @Test
    fun `onTextInput should update input`() = runTest {
        // given
        component.updateInstanceConfig(Instances.config)
        val id = activeModel.options.first().id
        val text = "text"
        // when
        component.onTextInput(id, text)
        // then
        assertThat(activeModel.options.firstOrNull { it.text == text }).isNotNull()
    }

    @Test
    fun `changeComponentAvailability should update component availability`() = runTest {
        // given
        // when
        component.changeComponentAvailability(true)
        // then
        assertThat(activeModel.pollButtonAvailable).isTrue()
        // and when
        component.changeComponentAvailability(false)
        // then
        assertThat(activeModel.pollButtonAvailable).isFalse()
    }

    @Test
    fun `toggleComponentVisibility should toggle component visibility`() = runTest {
        // given
        // when
        component.toggleComponentVisibility()
        // then
        assertThat(activeModel.pollContentVisible).isTrue()
        // when
        component.toggleComponentVisibility()
        // then
        assertThat(activeModel.pollContentVisible).isFalse()
    }

    @Test
    fun `option controls should add and remove options`() = runTest {
        // given
        component.updateInstanceConfig(Instances.config)
        // when
        component.onAddPollOptionClick()
        // then
        assertThat(activeModel.options.size).isEqualTo(3)
        // and when
        component.onDeletePollOptionClick(activeModel.options.first().id)
        // then
        assertThat(activeModel.options.size).isEqualTo(2)
    }

    override fun createComponent(): EditorPollComponent =
        EditorPollComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            dispatchers = testDispatchers,
        )
}
