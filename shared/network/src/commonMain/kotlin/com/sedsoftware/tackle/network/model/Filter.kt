package com.sedsoftware.tackle.network.model

import com.sedsoftware.tackle.network.model.type.FilterAction
import com.sedsoftware.tackle.network.model.type.FilterContext
import kotlinx.datetime.LocalDateTime

data class FilterResult(
    val filter: Filter,
    val keywordMatches: List<String>,
    val statusMatches: List<String>,
)

data class Filter(
    val id: String,
    val title: String,
    val context: List<FilterContext>,
    val expiresAt: LocalDateTime,
    val filterAction: FilterAction,
    val keywords: List<FilterKeyword>,
    val statuses: List<FilterStatus>,
)

data class FilterKeyword(
    val id: String,
    val keyword: String,
    val wholeWord: String,
)

data class FilterStatus(
    val id: String,
    val statusId: String,
)
