package com.sedsoftware.tackle.editor.poll.extension

import com.sedsoftware.tackle.editor.poll.model.PollOption

internal fun List<PollOption>.applyInput(id: String, text: String): List<PollOption> =
    map { item -> if (item.id == id) item.copy(text = text) else item }
