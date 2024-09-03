package com.sedsoftware.tackle.editor.poll.extension

import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption

internal fun List<PollChoiceOption>.applyInput(id: String, text: String): List<PollChoiceOption> =
    map { item -> if (item.id == id) item.copy(text = text) else item }
