package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

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
     * @param data the file to be attached, encoded using multipart form data. The file must have a MIME type.
     * @param onUpload callback being called during file uploading, triggered with the current uploading progress in percents
     * @param thumbnail the custom thumbnail of the media to be attached, encoded using multipart form data.
     * @param description a plain-text description of the media, for accessibility purposes.
     * @param focus Two floating points (x,y), comma-delimited, ranging from -1.0 to 1.0
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#v2">Upload media as an attachment</a>
     * @see <a href="https://docs.joinmastodon.org/api/guidelines/#focal-points">Focal points for cropping media thumbnails</a>
     */
    suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Int) -> Unit = {},
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
     * @param id the id of the MediaAttachment in the database.
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#get">Get media attachment</a>
     */
    suspend fun getFile(id: String): MediaAttachment

    /**
     * Update a MediaAttachment’s parameters, before it is attached to a status and posted.
     *
     * Requires Authorization Bearer header with the access token and write:media scope
     *
     * @param id the id of the MediaAttachment in the database.
     * @param thumbnail the custom thumbnail of the media to be attached, encoded using multipart form data.
     * @param description a plain-text description of the media, for accessibility purposes.
     * @param focus two floating points (x,y), comma-delimited, ranging from -1.0 to 1.0
     *
     * @see <a href="https://docs.joinmastodon.org/methods/media/#update">Update media attachment</a>
     */
    suspend fun updateFile(
        id: String,
        thumbnail: PlatformFileWrapper? = null,
        description: String? = null,
        focus: String? = null,
    ): MediaAttachment
}
