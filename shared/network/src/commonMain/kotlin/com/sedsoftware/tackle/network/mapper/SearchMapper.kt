package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.network.response.SearchResponse

internal object SearchMapper {

    fun map(from: SearchResponse): Search =
        Search(
            accounts = from.accounts.map(AccountMapper::map),
            statuses = from.statuses.map(StatusMapper::map),
            hashtags = from.hashtags.map(TagMapper::map),
        )
}
