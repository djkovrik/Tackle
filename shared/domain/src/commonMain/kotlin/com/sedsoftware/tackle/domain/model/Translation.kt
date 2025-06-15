package com.sedsoftware.tackle.domain.model

data class Translation(
    val content: String,
    val sourceLanguage: String,
    val language: String,
    val provider: String,
    val spoilerText: String = "",
    val poll: TranslatedPoll? = null,
    val attachments: List<TranslatedAttachment> = emptyList(),
)

data class TranslatedPoll(
    val id: String = "",
    val options: List<TranslatedPollOption> = emptyList(),
)

data class TranslatedPollOption(
    val title: String = "",
)

data class TranslatedAttachment(
    val id: String = "",
    val description: String = "",
)
