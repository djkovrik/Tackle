package com.sedsoftware.tackle.domain.model

data class Instance(
    val domain: String = "",
    val title: String = "",
    val version: String = "",
    val sourceUrl: String = "",
    val description: String = "",
    val activePerMonth: Long = 0L,
    val thumbnailUrl: String = "",
    val blurhash: String = "",
    val languages: List<String> = emptyList(),
    val contactEmail: String = "",
    val contactAccountId: String = "",
    val rules: List<Rule> = emptyList(),
    val config: Config = Config(),
) {

    data class Config(
        val accounts: Accounts = Accounts(),
        val mediaAttachments: MediaAttachments = MediaAttachments(),
        val polls: Polls = Polls(),
        val statuses: Statuses = Statuses(),
        val streamingUrl: String = "",
        val translationEnabled: Boolean = false,
    ) {

        data class Accounts(
            val maxFeatureTags: Long = 0L,
            val maxPinnedStatuses: Long = 0L,
        )

        data class MediaAttachments(
            val imageSizeLimit: Long = 0L,
            val imageMatrixLimit: Long = 0L,
            val videoSizeLimit: Long = 0L,
            val videoFrameRateLimit: Long = 0L,
            val videoMatrixLimit: Long = 0L,
            val supportedMimeTypes: List<String> = emptyList(),
        )

        data class Polls(
            val maxOptions: Long = 0L,
            val maxCharactersPerOption: Long = 0L,
            val minExpiration: Long = 0L,
            val maxExpiration: Long = 0L,
        )

        class Statuses(
            val maxCharacters: Long = 0L,
            val maxMediaAttachments: Long = 0L,
            val charactersReservedPerUrl: Long = 0L,
        )
    }

    companion object {
        fun empty(): Instance = Instance()
    }
}
