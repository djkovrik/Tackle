package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.Search

interface AuthorizedApi {

    /**
     * Confirms that the user token works.
     *
     * Requires Authorization Bearer header with the access token
     *
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#verify_credentials">Verify account credentials</a>
     */
    suspend fun verifyCredentials(): Account

    /**
     * Creates a media attachment to be used with a new status.
     * The full sized media will be processed asynchronously in the background for large uploads.
     *
     * Requires Authorization Bearer header with the access token and write:media scope
     *
     * @param file The file to be attached, encoded using multipart form data. The file must have a MIME type.
     * @param onUpload Callback being called during file uploading, triggered with the current uploading progress in percents
     * @param thumbnail The custom thumbnail of the media to be attached, encoded using multipart form data.
     * @param description A plain-text description of the media, for accessibility purposes.
     * @param focus Two floating points (x,y), comma-delimited, ranging from -1.0 to 1.0
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#v2">Upload media as an attachment</a>
     * @see <a href="https://docs.joinmastodon.org/api/guidelines/#focal-points">Focal points for cropping media thumbnails</a>
     */
    suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit = {},
        thumbnail: PlatformFileWrapper? = null,
        description: String? = null,
        focus: String? = null,
    ): MediaAttachment

    /**
     * Get a media attachment, before it is attached to a status and posted, but after it is accepted for processing.
     * Use this method to check that the full-sized media has finished processing.
     *
     * Requires Authorization Bearer header with the access token and write:media scope
     *
     * @param id The id of the MediaAttachment in the database.
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#get">Get media attachment</a>
     */
    suspend fun getFile(id: String): MediaAttachment

    /**
     * Update a MediaAttachment’s parameters, before it is attached to a status and posted.
     *
     * Requires Authorization Bearer header with the access token and write:media scope
     *
     * @param id The id of the MediaAttachment in the database.
     * @param thumbnail The custom thumbnail of the media to be attached, encoded using multipart form data.
     * @param description A plain-text description of the media, for accessibility purposes.
     * @param focus Two floating points (x,y), comma-delimited, ranging from -1.0 to 1.0
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#update">Update media attachment</a>
     */
    suspend fun updateFile(
        id: String,
        thumbnail: PlatformFileWrapper? = null,
        description: String? = null,
        focus: String? = null,
    ): MediaAttachment

    /**
     * Search for content in accounts, statuses and hashtags.
     *
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
     *
     * @see <a href="https://docs.joinmastodon.org/methods/search/#v2">Perform a search</a>
     */
    suspend fun search(
        query: String,
        type: String,
        resolve: Boolean? = null,
        following: Boolean? = null,
        accountId: String? = null,
        excludeUnreviewed: Boolean? = null,
        minId: String? = null,
        maxId: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Search
}
