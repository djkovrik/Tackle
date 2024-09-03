package com.sedsoftware.tackle.editor.extension

import com.sedsoftware.tackle.domain.model.NewStatusBundle

internal val NewStatusBundle.hasScheduledDate: Boolean
    get() = scheduledAtDate > 0L && scheduledAtHour >= 0 && scheduledAtMinute >= 0
