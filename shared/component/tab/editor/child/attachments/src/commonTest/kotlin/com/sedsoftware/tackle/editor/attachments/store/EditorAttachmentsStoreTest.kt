package com.sedsoftware.tackle.editor.attachments.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsDatabaseStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

internal class EditorAttachmentsStoreTest : StoreTest<Intent, State, Label>() {

    private val api: EditorAttachmentsApiStub = EditorAttachmentsApiStub()
    private val database: EditorAttachmentsDatabaseStub = EditorAttachmentsDatabaseStub()
    private val manager: EditorAttachmentsManager = EditorAttachmentsManager(api, database)

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    override fun createStore(): Store<Intent, State, Label> =
        EditorAttachmentsStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
