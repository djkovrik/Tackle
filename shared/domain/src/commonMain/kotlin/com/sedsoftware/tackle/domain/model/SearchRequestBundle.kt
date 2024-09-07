package com.sedsoftware.tackle.domain.model

/**
 * @param query The search query
 * @param type Specify whether to search for only accounts, hashtags, statuses
 * @param resolve Only relevant if type includes accounts. If true and (a) the search query is for a remote account the local server
 *                does not know about the account, WebFinger is used to try and resolve the account at someother.server. This provides
 *                the best recall at higher latency. If false only accounts the server knows about are returned.
 * @param following Only include accounts that the user is following? Defaults to false.
 * @param accountId If provided, will only return statuses authored by this account.
 * @param excludeUnreviewed Filter out unreviewed tags? Defaults to false. Use true when trying to find trending tags
 * @param minId returns results immediately newer than this ID. In effect, sets a cursor at this ID and paginates forward
 * @param maxId all results returned will be lesser than this ID. In effect, sets an upper bound on results
 * @param limit maximum number of results to return, per type. Defaults to 20 results per category. Max 40 results per category
 * @param offset skip the first n results
 */
data class SearchRequestBundle(
    val query: String,
    val type: String,
    val resolve: Boolean? = null,
    val following: Boolean? = null,
    val accountId: String? = null,
    val excludeUnreviewed: Boolean? = null,
    val minId: String? = null,
    val maxId: String? = null,
    val limit: Int? = null,
    val offset: Int? = null,
)
