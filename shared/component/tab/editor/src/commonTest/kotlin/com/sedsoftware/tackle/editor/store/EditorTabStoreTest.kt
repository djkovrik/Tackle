package com.sedsoftware.tackle.editor.store

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.store.EditorTabStore.Intent
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStore.State
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentDatabaseStub
import com.sedsoftware.tackle.editor.stubs.InstanceStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorTabStoreTest : StoreTest<Intent, State, Label>() {

    private val database: EditorTabComponentGateways.Database = EditorTabComponentDatabaseStub()
    private val manager: EditorTabManager = EditorTabManager(database)

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `store creation should load cached instance info`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.instanceInfoLoaded).isTrue()
        assertThat(store.state.instanceInfo.domain).isNotEmpty()
        assertThat(store.state.instanceInfo.config.statuses.maxMediaAttachments).isNotEqualTo(0)
        assertThat(labels.isNotEmpty())
        val label = labels.first()
        assertThat(label).hasClass(Label.InstanceConfigLoaded::class)
        label as Label.InstanceConfigLoaded
        assertThat(label.config).isEqualTo(InstanceStub.config)
    }

    override fun createStore(): Store<Intent, State, Label> =
        EditorTabStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
