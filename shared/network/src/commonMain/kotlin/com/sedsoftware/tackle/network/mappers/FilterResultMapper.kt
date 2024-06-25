package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Filter
import com.sedsoftware.tackle.network.model.FilterKeyword
import com.sedsoftware.tackle.network.model.FilterResult
import com.sedsoftware.tackle.network.model.FilterStatus
import com.sedsoftware.tackle.network.model.type.FilterAction
import com.sedsoftware.tackle.network.model.type.FilterContext
import com.sedsoftware.tackle.network.response.FilterKeywordResponse
import com.sedsoftware.tackle.network.response.FilterResponse
import com.sedsoftware.tackle.network.response.FilterResultResponse
import com.sedsoftware.tackle.network.response.FilterStatusResponse
import com.sedsoftware.tackle.network.response.type.FilterActionsRemote
import com.sedsoftware.tackle.network.response.type.FilterContextRemote
import com.sedsoftware.tackle.utils.toLocalDateTime

internal object FilterResultMapper {

    fun map(from: FilterResultResponse): FilterResult =
        FilterResult(
            filter = mapResponse(from.filter),
            keywordMatches = from.keywordMatches,
            statusMatches = from.statusMatches,
        )

    private fun mapResponse(from: FilterResponse): Filter =
        Filter(
            id = from.id,
            title = from.title,
            context = from.context.map { context ->
                when (context) {
                    FilterContextRemote.HOME -> FilterContext.HOME
                    FilterContextRemote.NOTIFICATIONS -> FilterContext.NOTIFICATIONS
                    FilterContextRemote.PUBLIC -> FilterContext.PUBLIC
                    FilterContextRemote.THREAD -> FilterContext.THREAD
                    FilterContextRemote.ACCOUNT -> FilterContext.ACCOUNT
                    FilterContextRemote.UNKNOWN -> FilterContext.UNKNOWN
                }
            },
            filterAction = when (from.filterAction) {
                FilterActionsRemote.WARN -> FilterAction.WARN
                FilterActionsRemote.HIDE -> FilterAction.HIDE
                FilterActionsRemote.UNKNOWN -> FilterAction.UNKNOWN
            },
            keywords = from.keywords.map(FilterResultMapper::mapKeyword),
            statuses = from.statuses.map(FilterResultMapper::mapStatus),
            expiresAt = from.expiresAt.toLocalDateTime(),
        )

    private fun mapKeyword(from: FilterKeywordResponse): FilterKeyword =
        FilterKeyword(
            id = from.id,
            keyword = from.keyword,
            wholeWord = from.wholeWord
        )

    private fun mapStatus(from: FilterStatusResponse): FilterStatus =
        FilterStatus(id = from.id, statusId = from.statusId)
}
