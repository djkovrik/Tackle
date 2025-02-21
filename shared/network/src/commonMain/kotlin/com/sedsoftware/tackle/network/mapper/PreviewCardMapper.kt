package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.PreviewCard
import com.sedsoftware.tackle.domain.model.PreviewCardAuthor
import com.sedsoftware.tackle.domain.model.type.PreviewCardType
import com.sedsoftware.tackle.network.response.PreviewCardResponse

internal object PreviewCardMapper {

    fun map(from: PreviewCardResponse): PreviewCard =
        PreviewCard(
            url = from.url,
            title = from.title,
            description = from.description,
            type = PreviewCardType.entries.firstOrNull { it.name.lowercase() == from.type }
                ?: PreviewCardType.UNKNOWN,
            authors = from.authors.map { author ->
                PreviewCardAuthor(
                    name = author.name,
                    url = author.url,
                    account = author.account?.let { AccountMapper.map(it) }
                )
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
