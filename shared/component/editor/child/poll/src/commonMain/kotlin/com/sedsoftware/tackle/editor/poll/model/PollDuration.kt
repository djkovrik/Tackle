package com.sedsoftware.tackle.editor.poll.model

enum class PollDuration(val seconds: Long) {
    NOT_SELECTED(0L),
    FIVE_MINUTES(300L),
    TEN_MINUTES(600L),
    THIRTY_MINUTES(1800L),
    ONE_HOUR(3600L),
    THREE_HOURS(10800L),
    SIX_HOURS(21600L),
    TWELVE_HOURS(43200L),
    ONE_DAY(86400L),
    TWO_DAYS(172800L),
    THREE_DAYS(259200L),
    SEVEN_DAYS(604800L),
    FOURTEEN_DAYS(1209600L),
    THIRTY_DAYS(2592000L);
}
