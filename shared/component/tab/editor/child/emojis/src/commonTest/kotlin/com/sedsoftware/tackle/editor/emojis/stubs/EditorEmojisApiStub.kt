package com.sedsoftware.tackle.editor.emojis.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import com.sedsoftware.tackle.utils.test.StubWithException

class EditorEmojisApiStub : StubWithException(), EditorEmojisGateways.Api {

    companion object {
        var correctResponse: List<CustomEmoji> = listOf(CustomEmoji("1", "2", "3", true, "4"))
    }

    var getServerEmojisResponse: List<CustomEmoji> = correctResponse

    override suspend fun getServerEmojis(): List<CustomEmoji> = asResponse(getServerEmojisResponse)
}
