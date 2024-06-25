package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.PreviewCard
import com.sedsoftware.tackle.network.model.type.PreviewCardType
import com.sedsoftware.tackle.network.response.PreviewCardResponse
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote

internal object PreviewCardMapper {

    fun map(from: PreviewCardResponse): PreviewCard =
        PreviewCard(
            url = from.url,
            title = from.title,
            description = from.description,
            type = when (from.type) {
                PreviewCardTypeRemote.LINK -> PreviewCardType.LINK
                PreviewCardTypeRemote.PHOTO -> PreviewCardType.PHOTO
                PreviewCardTypeRemote.VIDEO -> PreviewCardType.VIDEO
                PreviewCardTypeRemote.RICH -> PreviewCardType.RICH
                PreviewCardTypeRemote.UNKNOWN -> PreviewCardType.UNKNOWN
            },
            authorName = from.authorName,
            authorUrl = from.authorUrl,
            providerName = from.providerName,
            providerUrl = from.providerUrl,
            html = from.html,
            width = from.width,
            height = from.height,
            image = from.image,
            embedUrl = from.embedUrl,
            blurhash = from.blurhash,
        )
}
