package com.sedsoftware.tackle.editor.attachments.domain

import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsDatabaseStub

class EditorAttachmentsManagerTest {

    private val api: EditorAttachmentsApiStub = EditorAttachmentsApiStub()
    private val database: EditorAttachmentsDatabaseStub = EditorAttachmentsDatabaseStub()
    private val manager: EditorAttachmentsManager = EditorAttachmentsManager(api, database)


}
