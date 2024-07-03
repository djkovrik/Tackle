package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.network.response.InstanceConfigurationResponse
import com.sedsoftware.tackle.network.response.InstanceResponse
import com.sedsoftware.tackle.utils.orFalse
import com.sedsoftware.tackle.utils.orZero

internal object InstanceMapper {

    fun map(from: InstanceResponse): Instance =
        Instance(
            domain = from.domain,
            title = from.title,
            version = from.version,
            sourceUrl = from.sourceUrl,
            description = from.description,
            activePerMonth = from.usage?.users?.activePerMonth ?: 0L,
            thumbnailUrl = from.thumbnail?.url.orEmpty(),
            blurhash = from.thumbnail?.blurhash.orEmpty(),
            languages = from.languages,
            contactEmail = from.contact?.email.orEmpty(),
            contactAccountId = from.contact?.account?.id.orEmpty(),
            rules = from.rules.map(RuleMapper::map),
            config = mapConfig(from.configuration),
        )

    private fun mapConfig(configuration: InstanceConfigurationResponse?): Instance.Config {
        val from = configuration ?: return Instance.Config()

        return Instance.Config(
            accounts = Instance.Config.Accounts(
                maxFeatureTags = from.accounts?.maxFeatureTags.orZero(),
                maxPinnedStatuses = from.accounts?.maxPinnedStatuses.orZero(),
            ),
            mediaAttachments = Instance.Config.MediaAttachments(
                imageSizeLimit = from.attachments?.imageSizeLimit.orZero(),
                imageMatrixLimit = from.attachments?.imageMatrixLimit.orZero(),
                videoSizeLimit = from.attachments?.videoSizeLimit.orZero(),
                videoFrameRateLimit = from.attachments?.videoFrameRateLimit.orZero(),
                videoMatrixLimit = from.attachments?.videoMatrixLimit.orZero(),
                supportedMimeTypes = from.attachments?.supportedMimeTypes.orEmpty(),
            ),
            polls = Instance.Config.Polls(
                maxOptions = from.polls?.maxOptions.orZero(),
                maxCharactersPerOption = from.polls?.maxCharactersPerOption.orZero(),
                minExpiration = from.polls?.minExpiration.orZero(),
                maxExpiration = from.polls?.maxExpiration.orZero(),
            ),
            statuses = Instance.Config.Statuses(
                maxCharacters = from.statuses?.maxCharacters.orZero(),
                maxMediaAttachments = from.statuses?.maxMediaAttachments.orZero(),
                charactersReservedPerUrl = from.statuses?.charactersReservedPerUrl.orZero(),
            ),
            streamingUrl = from.urls?.streaming.orEmpty(),
            translationEnabled = from.translation?.enabled.orFalse(),
        )
    }
}
