package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.HashTag
import com.sedsoftware.tackle.domain.model.HashTagHistory
import com.sedsoftware.tackle.network.response.HashTagResponse

internal object TagMapper {

    fun map(from: HashTagResponse): HashTag =
        HashTag(
            name = from.name,
            url = from.url,
            history = from.history.map { HashTagHistory(it.day, it.uses, it.accounts) },
            following = from.following,
            trendable = from.trendable,
            usable = from.usable,
            requiresReview = from.requiresReview,
        )
}
