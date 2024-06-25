package com.sedsoftware.tackle.network.response

import com.sedsoftware.tackle.network.response.type.FilterActionsRemote
import com.sedsoftware.tackle.network.response.type.FilterContextRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class FilterResultResponse(
    @SerialName("filter") val filter: FilterResponse = FilterResponse(),
    @SerialName("keyword_matches") val keywordMatches: List<String> = emptyList(),
    @SerialName("status_matches") val statusMatches: List<String> = emptyList(),
)

@Serializable
internal class FilterResponse(
    @SerialName("id") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("context") val context: List<FilterContextRemote> = emptyList(),
    @SerialName("filter_action") val filterAction: FilterActionsRemote = FilterActionsRemote.UNKNOWN,
    @SerialName("keywords") val keywords: List<FilterKeywordResponse> = emptyList(),
    @SerialName("statuses") val statuses: List<FilterStatusResponse> = emptyList(),
    @SerialName("expires_at") val expiresAt: String = "",
)

@Serializable
internal class FilterKeywordResponse(
    @SerialName("id") val id: String = "",
    @SerialName("keyword") val keyword: String = "",
    @SerialName("whole_word") val wholeWord: String = "",
)

@Serializable
internal class FilterStatusResponse(
    @SerialName("id") val id: String = "",
    @SerialName("status_id") val statusId: String = "",
)
