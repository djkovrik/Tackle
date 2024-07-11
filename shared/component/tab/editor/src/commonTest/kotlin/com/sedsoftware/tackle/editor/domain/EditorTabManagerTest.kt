package com.sedsoftware.tackle.editor.domain

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentDatabaseStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorTabManagerTest {

    private val database: EditorTabComponentGateways.Database = EditorTabComponentDatabaseStub()
    private val manager: EditorTabManager = EditorTabManager(database)

    @Test
    fun `getCachedInstanceInfo should return instance info`() = runTest {
        // given
        // when
        val result = manager.getCachedInstanceInfo()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().domain).isNotEmpty()
    }
}
