package com.sedsoftware.tackle.editor.poll.stubs

import com.sedsoftware.tackle.domain.model.Instance

internal object InstanceConfigStub {
    val config: Instance.Config = Instance.Config(
        polls = Instance.Config.Polls(
            maxOptions = 4,
            maxCharactersPerOption = 60,
            minExpiration = 500L,
            maxExpiration = 172800L,
        )
    )
}
