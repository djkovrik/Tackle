package com.sedsoftware.tackle.domain.model

class Translation(
    val content: String,
    val spoilerText: String,
    val poll: TranslatedPoll?,
    val attachments: List<TranslatedAttachment>,
    val language: String,
    val provider: String,
)

class TranslatedPoll(
    val id: String = "",
    val options: List<TranslatedPollOption> = emptyList(),
)

class TranslatedPollOption(
    val title: String = "",
)

class TranslatedAttachment(
    val id: String = "",
    val description: String = "",
)
