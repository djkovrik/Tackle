package com.sedsoftware.tackle.compose.extension

import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_time_days
import tackle.shared.compose.generated.resources.common_time_hours
import tackle.shared.compose.generated.resources.common_time_minutes
import tackle.shared.compose.generated.resources.common_time_months
import tackle.shared.compose.generated.resources.common_time_now
import tackle.shared.compose.generated.resources.common_time_seconds
import tackle.shared.compose.generated.resources.common_time_years

fun ShortDateUnit.getSimplifiedDate(): Pair<Int, StringResource> = when (this) {
    is ShortDateUnit.Now -> -1 to Res.string.common_time_now
    is ShortDateUnit.Seconds -> value to Res.string.common_time_seconds
    is ShortDateUnit.Minutes -> value to Res.string.common_time_minutes
    is ShortDateUnit.Hours -> value to Res.string.common_time_hours
    is ShortDateUnit.Days -> value to Res.string.common_time_days
    is ShortDateUnit.Months -> value to Res.string.common_time_months
    is ShortDateUnit.Years -> value to Res.string.common_time_years
}
