package com.sedsoftware.tackle.editor.poll

import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.domain.model.Instance.Config.Polls

internal object Instances {
    val config: Config = Config(
        polls = Polls(
            maxOptions = 4,
            maxCharactersPerOption = 60,
            minExpiration = 500L,
            maxExpiration = 172800L,
        )
    )
}
