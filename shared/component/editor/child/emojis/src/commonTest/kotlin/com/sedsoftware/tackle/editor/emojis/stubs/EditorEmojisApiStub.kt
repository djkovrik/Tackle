package com.sedsoftware.tackle.editor.emojis.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.Responses
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorEmojisApiStub : BaseStub(), EditorEmojisGateways.Api {

    var getServerEmojisResponse: List<CustomEmoji> = Responses.correctResponse

    override suspend fun getServerEmojis(): List<CustomEmoji> =
        asResponse(getServerEmojisResponse)
}
