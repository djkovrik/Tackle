package com.sedsoftware.tackle.editor.poll.store

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.poll.domain.EditorPollManager
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore
import com.sedsoftware.tackle.editor.poll.stubs.InstanceConfigStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorPollStoreTest : StoreTest<EditorPollStore.Intent, EditorPollStore.State, Nothing>() {

    private val manager: EditorPollManager = EditorPollManager()

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `UpdateInstanceConfig should update state config`() = runTest {
        // given
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))
        // then
        assertThat(store.state.config).isEqualTo(config)
        assertThat(store.state.configLoaded).isTrue()
    }

    @Test
    fun `UpdateInstanceConfig should populate available durations`() = runTest {
        // given
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))
        // then
        assertThat(store.state.availableDurations).isNotEmpty()
        assertThat(store.state.duration).isEqualTo(store.state.availableDurations.first())
    }

    @Test
    fun `UpdateInstanceConfig should populate default options`() = runTest {
        // given
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))
        // then
        assertThat(store.state.options).isNotEmpty()
        val firstOption = store.state.options.first()
        val lastOption = store.state.options.last()
        assertThat(firstOption.id).isNotEqualTo(lastOption.id)
    }

    @Test
    fun `ChangeComponentAvailability should update component availability`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorPollStore.Intent.ChangeComponentAvailability(false))
        // then
        assertThat(store.state.pollAvailable).isFalse()
        // and when
        store.accept(EditorPollStore.Intent.ChangeComponentAvailability(true))
        // then
        assertThat(store.state.pollAvailable).isTrue()
    }

    @Test
    fun `ToggleComponentVisibility should update component visibility`() = runTest {
        // given
        store.init()
        // when
        store.accept(EditorPollStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.pollVisible).isTrue()
        // when
        store.accept(EditorPollStore.Intent.ToggleComponentVisibility)
        // then
        assertThat(store.state.pollVisible).isFalse()
    }

    @Test
    fun `OnRequestDurationPicker should change picker visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorPollStore.Intent.OnRequestDurationPicker(true))
        // then
        assertThat(store.state.durationPickerVisible).isTrue()
        // and when
        store.accept(EditorPollStore.Intent.OnRequestDurationPicker(false))
        // then
        assertThat(store.state.durationPickerVisible).isFalse()

    }

    @Test
    fun `OnDurationSelected should update selected duration`() = runTest {
        // given
        val selected = PollDuration.THIRTY_DAYS
        // when
        store.init()
        store.accept(EditorPollStore.Intent.OnDurationSelected(selected))
        // then
        assertThat(store.state.duration).isEqualTo(selected)
    }

    @Test
    fun `OnMultiselectEnabled should update state`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorPollStore.Intent.OnMultiselectEnabled(true))
        // then
        assertThat(store.state.multiselectEnabled).isTrue()
        // and when
        store.accept(EditorPollStore.Intent.OnMultiselectEnabled(false))
        // then
        assertThat(store.state.multiselectEnabled).isFalse()
    }

    @Test
    fun `OnTextInput should update related poll item`() = runTest {
        // given
        val text = "text text"
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))
        val lastOption = store.state.options.lastOrNull()
        // then
        assertThat(lastOption).isNotNull()
        lastOption!!
        assertThat(lastOption.id).isNotEmpty()
        assertThat(lastOption.text).isEmpty()
        // and when
        val lastOptionId = lastOption.id
        store.accept(EditorPollStore.Intent.OnTextInput(lastOptionId, text))
        // then
        assertThat(store.state.options.last().text).isEqualTo(text)
    }

    @Test
    fun `OnAddPollOption and OnDeletePollOption should update state and controls`() = runTest {
        // given
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))

        // then has two options, can insert and can't delete
        assertThat(store.state.options.size).isEqualTo(2)
        assertThat(store.state.insertionAvailable).isTrue()
        assertThat(store.state.deletionAvailable).isFalse()

        // when adding new option
        store.accept(EditorPollStore.Intent.OnAddPollOption)

        // then has three options, can insert and can delete
        assertThat(store.state.options.size).isEqualTo(3)
        assertThat(store.state.insertionAvailable).isTrue()
        assertThat(store.state.deletionAvailable).isTrue()

        // when adding new option
        store.accept(EditorPollStore.Intent.OnAddPollOption)

        // then has four options, can't insert and can delete
        assertThat(store.state.options.size).isEqualTo(4)
        assertThat(store.state.insertionAvailable).isFalse()
        assertThat(store.state.deletionAvailable).isTrue()

        // when deleting an option
        store.accept(EditorPollStore.Intent.OnDeletePollOption(store.state.options.first().id))

        // then has three options, can insert and can delete
        assertThat(store.state.options.size).isEqualTo(3)
        assertThat(store.state.insertionAvailable).isTrue()
        assertThat(store.state.deletionAvailable).isTrue()

        // when deleting an option
        store.accept(EditorPollStore.Intent.OnDeletePollOption(store.state.options.first().id))

        // then has two options, can insert and can't delete
        assertThat(store.state.options.size).isEqualTo(2)
        assertThat(store.state.insertionAvailable).isTrue()
        assertThat(store.state.deletionAvailable).isFalse()
    }

    override fun createStore(): Store<EditorPollStore.Intent, EditorPollStore.State, Nothing> =
        EditorPollStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
