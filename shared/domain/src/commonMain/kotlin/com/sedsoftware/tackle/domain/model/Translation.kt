package com.sedsoftware.tackle.domain.model

class Translation(
    val content: String,
    val sourceLanguage: String,
    val language: String,
    val provider: String,
    val spoilerText: String = "",
    val poll: TranslatedPoll? = null,
    val attachments: List<TranslatedAttachment> = emptyList(),
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
