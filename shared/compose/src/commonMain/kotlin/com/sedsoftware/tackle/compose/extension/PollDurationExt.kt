package com.sedsoftware.tackle.compose.extension

import com.sedsoftware.tackle.editor.poll.model.PollDuration
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_poll_duration
import tackle.shared.compose.generated.resources.editor_poll_duration_day
import tackle.shared.compose.generated.resources.editor_poll_duration_five_days
import tackle.shared.compose.generated.resources.editor_poll_duration_five_minutes
import tackle.shared.compose.generated.resources.editor_poll_duration_four_days
import tackle.shared.compose.generated.resources.editor_poll_duration_fourteen_days
import tackle.shared.compose.generated.resources.editor_poll_duration_hour
import tackle.shared.compose.generated.resources.editor_poll_duration_seven_days
import tackle.shared.compose.generated.resources.editor_poll_duration_six_days
import tackle.shared.compose.generated.resources.editor_poll_duration_six_hours
import tackle.shared.compose.generated.resources.editor_poll_duration_ten_minutes
import tackle.shared.compose.generated.resources.editor_poll_duration_thirty_days
import tackle.shared.compose.generated.resources.editor_poll_duration_thirty_minutes
import tackle.shared.compose.generated.resources.editor_poll_duration_three_days
import tackle.shared.compose.generated.resources.editor_poll_duration_three_hours
import tackle.shared.compose.generated.resources.editor_poll_duration_twelve_hours
import tackle.shared.compose.generated.resources.editor_poll_duration_two_days

internal fun PollDuration.getTitle(): StringResource = when (this) {
    PollDuration.NOT_SELECTED -> Res.string.editor_poll_duration
    PollDuration.FIVE_MINUTES -> Res.string.editor_poll_duration_five_minutes
    PollDuration.TEN_MINUTES -> Res.string.editor_poll_duration_ten_minutes
    PollDuration.THIRTY_MINUTES -> Res.string.editor_poll_duration_thirty_minutes
    PollDuration.ONE_HOUR -> Res.string.editor_poll_duration_hour
    PollDuration.THREE_HOURS -> Res.string.editor_poll_duration_three_hours
    PollDuration.SIX_HOURS -> Res.string.editor_poll_duration_six_hours
    PollDuration.TWELVE_HOURS -> Res.string.editor_poll_duration_twelve_hours
    PollDuration.ONE_DAY -> Res.string.editor_poll_duration_day
    PollDuration.TWO_DAYS -> Res.string.editor_poll_duration_two_days
    PollDuration.THREE_DAYS -> Res.string.editor_poll_duration_three_days
    PollDuration.FOUR_DAYS -> Res.string.editor_poll_duration_four_days
    PollDuration.FIVE_DAYS -> Res.string.editor_poll_duration_five_days
    PollDuration.SIX_DAYS -> Res.string.editor_poll_duration_six_days
    PollDuration.SEVEN_DAYS -> Res.string.editor_poll_duration_seven_days
    PollDuration.FOURTEEN_DAYS -> Res.string.editor_poll_duration_fourteen_days
    PollDuration.THIRTY_DAYS -> Res.string.editor_poll_duration_thirty_days
}
