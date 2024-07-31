package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.Tag
import com.sedsoftware.tackle.domain.model.TagHistory
import com.sedsoftware.tackle.network.response.TagResponse

internal object TagMapper {

    fun map(from: TagResponse): Tag =
        Tag(
            name = from.name,
            url = from.url,
            history = from.history.map { TagHistory(it.day, it.uses, it.accounts) },
            following = from.following,
            trendable = from.trendable,
            usable = from.usable,
            requiresReview = from.requiresReview,
        )
}
