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
            val maxOptions: Int = 0,
            val maxCharactersPerOption: Int = 0,
            val minExpiration: Long = 0L,
            val maxExpiration: Long = 0L,
        )

        class Statuses(
            val maxCharacters: Int = 0,
            val maxMediaAttachments: Int = 0,
            val charactersReservedPerUrl: Int = 0,
        )
    }

    companion object {
        fun empty(): Instance = Instance()
    }
}
