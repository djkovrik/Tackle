package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.model.CachedConfig
import com.sedsoftware.tackle.database.model.CachedConfig.CachedAccounts
import com.sedsoftware.tackle.database.model.CachedConfig.CachedMediaAttachments
import com.sedsoftware.tackle.database.model.CachedConfig.CachedPolls
import com.sedsoftware.tackle.database.model.CachedConfig.CachedStatuses
import com.sedsoftware.tackle.domain.model.Instance

internal object CachedConfigMapper {

    fun toCache(from: Instance.Config): CachedConfig = CachedConfig(
        accounts = CachedAccounts(
            maxFeatureTags = from.accounts.maxFeatureTags,
            maxPinnedStatuses = from.accounts.maxPinnedStatuses,
        ),
        mediaAttachments = CachedMediaAttachments(
            imageSizeLimit = from.mediaAttachments.imageSizeLimit,
            imageMatrixLimit = from.mediaAttachments.imageMatrixLimit,
            videoSizeLimit = from.mediaAttachments.videoSizeLimit,
            videoFrameRateLimit = from.mediaAttachments.videoFrameRateLimit,
            videoMatrixLimit = from.mediaAttachments.videoMatrixLimit,
            supportedMimeTypes = from.mediaAttachments.supportedMimeTypes,
        ),
        polls = CachedPolls(
            maxOptions = from.polls.maxOptions,
            maxCharactersPerOption = from.polls.maxCharactersPerOption,
            minExpiration = from.polls.minExpiration,
            maxExpiration = from.polls.maxExpiration,
        ),
        statuses = CachedStatuses(
            maxCharacters = from.statuses.maxCharacters,
            maxMediaAttachments = from.statuses.maxMediaAttachments,
            charactersReservedPerUrl = from.statuses.charactersReservedPerUrl,
        ),
        streamingUrl = from.streamingUrl,
        translationEnabled = from.translationEnabled,
    )

    fun fromCache(from: CachedConfig): Instance.Config = Instance.Config(
        accounts = Instance.Config.Accounts(
            maxFeatureTags = from.accounts.maxFeatureTags,
            maxPinnedStatuses = from.accounts.maxPinnedStatuses,
        ),
        mediaAttachments = Instance.Config.MediaAttachments(
            imageSizeLimit = from.mediaAttachments.imageSizeLimit,
            imageMatrixLimit = from.mediaAttachments.imageMatrixLimit,
            videoSizeLimit = from.mediaAttachments.videoSizeLimit,
            videoFrameRateLimit = from.mediaAttachments.videoFrameRateLimit,
            videoMatrixLimit = from.mediaAttachments.videoMatrixLimit,
            supportedMimeTypes = from.mediaAttachments.supportedMimeTypes,
        ),
        polls = Instance.Config.Polls(
            maxOptions = from.polls.maxOptions,
            maxCharactersPerOption = from.polls.maxCharactersPerOption,
            minExpiration = from.polls.minExpiration,
            maxExpiration = from.polls.maxExpiration,
        ),
        statuses = Instance.Config.Statuses(
            maxCharacters = from.statuses.maxCharacters,
            maxMediaAttachments = from.statuses.maxMediaAttachments,
            charactersReservedPerUrl = from.statuses.charactersReservedPerUrl,
        ),
        streamingUrl = from.streamingUrl,
        translationEnabled = from.translationEnabled,
    )
}
