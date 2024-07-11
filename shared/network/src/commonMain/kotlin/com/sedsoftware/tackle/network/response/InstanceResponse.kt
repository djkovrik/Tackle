package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class InstanceResponse(
    @SerialName("domain") val domain: String,
    @SerialName("title") val title: String,
    @SerialName("version") val version: String,
    @SerialName("source_url") val sourceUrl: String,
    @SerialName("description") val description: String,
    @SerialName("usage") val usage: InstanceUsersResponse? = null,
    @SerialName("thumbnail") val thumbnail: InstanceThumbnailResponse? = null,
    @SerialName("languages") val languages: List<String> = emptyList(),
    @SerialName("configuration") val configuration: InstanceConfigurationResponse? = null,
    @SerialName("contact") val contact: ContactAccountResponse? = null,
    @SerialName("rules") val rules: List<RuleItemResponse> = emptyList(),
)

@Serializable
internal class InstanceUsersResponse(
    @SerialName("users") val users: InstanceActiveMonthResponse,
)

@Serializable
internal class InstanceActiveMonthResponse(
    @SerialName("active_month") val activePerMonth: Long,
)

@Serializable
internal class InstanceThumbnailResponse(
    @SerialName("url") val url: String,
    @SerialName("blurhash") val blurhash: String? = null,
    @SerialName("versions") val versions: Map<String, String> = emptyMap(),
)

@Serializable
internal class InstanceConfigurationAccountsResponse(
    @SerialName("max_featured_tags") val maxFeatureTags: Long = 0,
    @SerialName("max_pinned_statuses") val maxPinnedStatuses: Long = 0,
)

@Serializable
internal class InstanceConfigurationAttachmentsResponse(
    @SerialName("image_size_limit") val imageSizeLimit: Long = 0L,
    @SerialName("image_matrix_limit") val imageMatrixLimit: Long = 0L,
    @SerialName("video_size_limit") val videoSizeLimit: Long = 0L,
    @SerialName("video_frame_rate_limit") val videoFrameRateLimit: Long = 0L,
    @SerialName("video_matrix_limit") val videoMatrixLimit: Long = 0L,
    @SerialName("supported_mime_types") val supportedMimeTypes: List<String> = emptyList(),
)

@Serializable
internal class InstanceConfigurationPollsResponse(
    @SerialName("max_options") val maxOptions: Int = 0,
    @SerialName("max_characters_per_option") val maxCharactersPerOption: Int = 0,
    @SerialName("min_expiration") val minExpiration: Long = 0L,
    @SerialName("max_expiration") val maxExpiration: Long = 0L,
)

@Serializable
internal class InstanceConfigurationStatusesResponse(
    @SerialName("max_characters") val maxCharacters: Int = 0,
    @SerialName("max_media_attachments") val maxMediaAttachments: Int = 0,
    @SerialName("characters_reserved_per_url") val charactersReservedPerUrl: Int = 0,
)

@Serializable
internal class InstanceConfigurationTranslationResponse(
    @SerialName("enabled") val enabled: Boolean = true,
)

@Serializable
internal class InstanceConfigurationUrlsResponse(
    @SerialName("streaming") val streaming: String = "",
)

@Serializable
internal class InstanceConfigurationResponse(
    @SerialName("accounts") val accounts: InstanceConfigurationAccountsResponse? = null,
    @SerialName("media_attachments") val attachments: InstanceConfigurationAttachmentsResponse? = null,
    @SerialName("polls") val polls: InstanceConfigurationPollsResponse? = null,
    @SerialName("statuses") val statuses: InstanceConfigurationStatusesResponse? = null,
    @SerialName("translation") val translation: InstanceConfigurationTranslationResponse? = null,
    @SerialName("urls") val urls: InstanceConfigurationUrlsResponse? = null,
)

@Serializable
internal class RuleItemResponse(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("hint") val hint: String = "",
)

@Serializable
internal class ContactAccountResponse(
    @SerialName("email") val email: String = "",
    @SerialName("account") val account: AccountResponse? = null,
)
