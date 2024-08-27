package com.sedsoftware.tackle.domain.model

import com.sedsoftware.tackle.domain.model.type.StatusVisibility

data class NewStatusBundle(
    val status: String,
    val language: String,
    val visibility: StatusVisibility,
    val mediaIds: List<String>,
    val pollOptions: List<String>,
    val pollExpiresIn: Long,
    val pollAllowMultiple: Boolean,
    val pollHideTotals: Boolean,
    val inReplyToId: String,
    val sensitive: Boolean,
    val spoilerText: String,
    val scheduledAtDate: Long,
    val scheduledAtHour: Int,
    val scheduledAtMinute: Int,
) {
    @Suppress("TooManyFunctions")
    data class Builder(
        private var status: String = "",
        private var language: String = "",
        private var visibility: StatusVisibility = StatusVisibility.PUBLIC,
        private var mediaIds: List<String> = emptyList(),
        private var pollOptions: List<String> = emptyList(),
        private var pollExpiresIn: Long = -1L,
        private var pollAllowMultiple: Boolean = false,
        private var pollHideTotals: Boolean = false,
        private var inReplyToId: String = "",
        private var sensitive: Boolean = false,
        private var spoilerText: String = "",
        private var scheduledAtDate: Long = -1L,
        private var scheduledAtHour: Int = -1,
        private var scheduledAtMinute: Int = -1,
    ) {

        fun status(status: String) = apply { this.status = status }
        fun language(language: String) = apply { this.language = language }
        fun visibility(visibility: StatusVisibility) = apply { this.visibility = visibility }
        fun mediaIds(mediaIds: List<String>) = apply { this.mediaIds = mediaIds }
        fun pollOptions(pollOptions: List<String>) = apply { this.pollOptions = pollOptions }
        fun pollExpiresIn(pollExpiresIn: Long) = apply { this.pollExpiresIn = pollExpiresIn }
        fun pollAllowMultiple(pollAllowMultiple: Boolean) = apply { this.pollAllowMultiple = pollAllowMultiple }
        fun pollHideTotals(pollHideTotals: Boolean) = apply { this.pollHideTotals = pollHideTotals }
        fun inReplyToId(inReplyToId: String) = apply { this.inReplyToId = inReplyToId }
        fun sensitive(sensitive: Boolean) = apply { this.sensitive = sensitive }
        fun spoilerText(spoilerText: String) = apply { this.spoilerText = spoilerText }
        fun scheduledAtDate(scheduledAtDate: Long) = apply { this.scheduledAtDate = scheduledAtDate }
        fun scheduledAtHour(scheduledAtHour: Int) = apply { this.scheduledAtHour = scheduledAtHour }
        fun scheduledAtMinute(scheduledAtMinute: Int) = apply { this.scheduledAtMinute = scheduledAtMinute }

        fun build(): NewStatusBundle = NewStatusBundle(
            status = status,
            language = language,
            visibility = visibility,
            mediaIds = mediaIds,
            pollOptions = pollOptions,
            pollExpiresIn = pollExpiresIn,
            pollAllowMultiple = pollAllowMultiple,
            pollHideTotals = pollHideTotals,
            inReplyToId = inReplyToId,
            sensitive = sensitive,
            spoilerText = spoilerText,
            scheduledAtDate = scheduledAtDate,
            scheduledAtHour = scheduledAtHour,
            scheduledAtMinute = scheduledAtMinute,
        )
    }
}
