package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.SearchRequestBundle
import com.sedsoftware.tackle.domain.model.Status

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
    suspend fun updateFile(id: String, thumbnail: PlatformFileWrapper?, description: String?, focus: String?): MediaAttachment

    /**
     * Search for content in accounts, statuses and hashtags.
     *
     * @param bundle Search request params
     *
     * @see <a href="https://docs.joinmastodon.org/methods/search/#v2">Perform a search</a>
     */
    suspend fun search(bundle: SearchRequestBundle): Search

    /**
     * Publish a status with the given parameters
     *
     * @param bundle Status content bundle
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#create">Post a new status</a>
     */
    suspend fun sendStatus(bundle: NewStatusBundle): Status

    /**
     * Publish a scheduled status with the given parameters
     *
     * @param bundle Status content bundle
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#create">Post a new status</a>
     */
    suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus
}
