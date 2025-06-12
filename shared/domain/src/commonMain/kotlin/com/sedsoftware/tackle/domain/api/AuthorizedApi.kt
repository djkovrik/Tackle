@file:Suppress("TooManyFunctions")
package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.SearchRequestBundle
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation

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

    /**
     * Delete one of your own statuses
     *
     * @param id The ID of the Status in the database
     * @param deleteMedia Whether to immediately delete the post’s media attachments
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#delete">Delete a status</a>
     */
    suspend fun deleteStatus(id: String, deleteMedia: Boolean): Status

    /**
     * Translate the status content into some language
     *
     * @param id The ID of the Status in the database
     * @param lang The status content will be translated into this language. Defaults to the user’s current locale.
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#translate">Translate a status</a>
     */
    suspend fun translateStatus(id: String, lang: String) : Translation

    /**
     * Add a status to your favourites list.
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#favourite">Favourite a status</a>
     */
    suspend fun favouriteStatus(id: String): Status

    /**
     * Remove a status from your favourites list
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#unfavourite">Undo favourite of a status</a>
     */
    suspend fun unfavouriteStatus(id: String): Status

    /**
     * Reshare a status on your own profile
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#boost">Boost a status</a>
     */
    suspend fun boostStatus(id: String): Status

    /**
     * Undo a reshare of a status
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#unreblog">Undo boost of a status</a>
     */
    suspend fun unboostStatus(id: String): Status

    /**
     * Privately bookmark a status
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#bookmark">Bookmark a status</a>
     */
    suspend fun bookmarkStatus(id: String): Status

    /**
     * Remove a status from your private bookmarks
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#unbookmark">Undo bookmark of a status</a>
     */
    suspend fun unbookmarkStatus(id: String): Status

    /**
     * Feature one of your own public statuses at the top of your profile
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#pin">Pin status to profile</a>
     */
    suspend fun pinStatus(id: String): Status

    /**
     * Unfeature a status from the top of your profile
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#unpin">Unpin status from profile</a>
     */
    suspend fun unpinStatus(id: String): Status

    /**
     * Do not receive notifications for the thread that this status is part of. Must be a thread in which you are a participant.
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#mute">Mute a conversation</a>
     */
    suspend fun muteStatus(id: String): Status

    /**
     * Start receiving notifications again for the thread that this status is part of
     *
     * @param id The ID of the Status in the database
     *
     * @see <a href="https://docs.joinmastodon.org/methods/statuses/#unmute">Unmute a conversation</a>
     */
    suspend fun unmuteStatus(id: String): Status

    /**
     * Vote on a poll attached to a status
     *
     * @param id The ID of the Poll in the database
     * @param choices Provide your own votes as an index for each option (starting from 0)
     *
     * @see <a href="https://docs.joinmastodon.org/methods/polls/#vote">Vote on a poll</a>
     */
    suspend fun votePoll(id: String, choices: List<Int>): Poll
}
