package com.sedsoftware.tackle.database.model

import kotlinx.serialization.Serializable

@Serializable
internal class CachedConfig(
    val accounts: CachedAccounts,
    val mediaAttachments: CachedMediaAttachments = CachedMediaAttachments(),
    val polls: CachedPolls = CachedPolls(),
    val statuses: CachedStatuses = CachedStatuses(),
    val streamingUrl: String = "",
    val translationEnabled: Boolean = false,
) {

    @Serializable
    class CachedAccounts(
        val maxFeatureTags: Long = 0L,
        val maxPinnedStatuses: Long = 0L,
    )

    @Serializable
    class CachedMediaAttachments(
        val imageSizeLimit: Long = 0L,
        val imageMatrixLimit: Long = 0L,
        val videoSizeLimit: Long = 0L,
        val videoFrameRateLimit: Long = 0L,
        val videoMatrixLimit: Long = 0L,
        val supportedMimeTypes: List<String> = emptyList(),
    )

    @Serializable
    class CachedPolls(
        val maxOptions: Int = 0,
        val maxCharactersPerOption: Int = 0,
        val minExpiration: Long = 0L,
        val maxExpiration: Long = 0L,
    )

    @Serializable
    class CachedStatuses(
        val maxCharacters: Int = 0,
        val maxMediaAttachments: Int = 0,
        val charactersReservedPerUrl: Int = 0,
    )
}
