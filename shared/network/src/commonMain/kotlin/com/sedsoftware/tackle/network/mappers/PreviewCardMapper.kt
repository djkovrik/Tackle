package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.PreviewCard
import com.sedsoftware.tackle.domain.model.type.PreviewCardType
import com.sedsoftware.tackle.network.response.PreviewCardResponse
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote.LINK
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote.PHOTO
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote.RICH
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote.UNKNOWN
import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote.VIDEO

internal object PreviewCardMapper {

    fun map(from: PreviewCardResponse): PreviewCard =
        PreviewCard(
            url = from.url,
            title = from.title,
            description = from.description,
            type = when (from.type) {
                LINK -> PreviewCardType.LINK
                PHOTO -> PreviewCardType.PHOTO
                VIDEO -> PreviewCardType.VIDEO
                RICH -> PreviewCardType.RICH
                UNKNOWN -> PreviewCardType.UNKNOWN
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
