package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TranslationResponse(
    @SerialName("content") val content: String = "",
    @SerialName("spoiler_text") val spoilerText: String = "",
    @SerialName("poll") val poll: TranslatedPollResponse? = null,
    @SerialName("media_attachments") val attachments: List<TranslatedAttachmentResponse> = emptyList(),
    @SerialName("detected_source_language") val sourceLanguage: String = "",
    @SerialName("language") val language: String = "",
    @SerialName("provider") val provider: String = "",
)

@Serializable
internal class TranslatedPollResponse(
    @SerialName("id") val id: String = "",
    @SerialName("options") val options: List<TranslatedPollOptionResponse> = emptyList(),
)

@Serializable
internal class TranslatedPollOptionResponse(
    @SerialName("title") val title: String = "",
)

@Serializable
internal class TranslatedAttachmentResponse(
    @SerialName("id") val id: String = "",
    @SerialName("description") val description: String = "",
)
