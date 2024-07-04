package com.sedsoftware.tackle.editor.emojis.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways

class EditorEmojisApiStub : EditorEmojisGateways.Api {

    companion object {
        var correctResponse: List<CustomEmoji> = listOf(CustomEmoji("1", "2", "3", true, "4"))
        var emptyResponse: List<CustomEmoji> = emptyList()
        var shouldThrowException: Boolean = false
    }

    var getServerEmojisResponse: List<CustomEmoji> = correctResponse

    override suspend fun getServerEmojis(): List<CustomEmoji> =
        if (!shouldThrowException) {
            getServerEmojisResponse
        } else {
            error("getServerEmojis exception")
        }
}
