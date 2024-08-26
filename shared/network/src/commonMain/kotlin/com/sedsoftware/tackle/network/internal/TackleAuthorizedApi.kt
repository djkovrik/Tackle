package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.network.mapper.AccountMapper
import com.sedsoftware.tackle.network.mapper.MediaAttachmentMapper
import com.sedsoftware.tackle.network.mapper.ScheduledStatusMapper
import com.sedsoftware.tackle.network.mapper.SearchMapper
import com.sedsoftware.tackle.network.mapper.StatusMapper
import com.sedsoftware.tackle.network.request.CreateStatusRequest
import com.sedsoftware.tackle.network.request.CreateStatusRequestPoll
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.network.response.SearchResponse
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.utils.DateTimeUtils
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

internal class TackleAuthorizedApi(
    domainProvider: () -> String,
    tokenProvider: () -> String,
) : BaseApi(domainProvider = domainProvider, tokenProvider = tokenProvider), AuthorizedApi {


    override suspend fun verifyCredentials(): Account =
        doRequest<AccountResponse, Account>(
            requestUrl = "$instanceUrl/api/v1/accounts/verify_credentials",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = AccountMapper::map,
        )

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v2/media",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            val fileData = file.readBytes.invoke()
            val thumbnailData = thumbnail?.readBytes?.invoke()

            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", fileData, Headers.build {
                            append(HttpHeaders.ContentType, file.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                        })

                        if (thumbnail != null && thumbnailData != null) {
                            append("thumbnail", thumbnailData, Headers.build {
                                append(HttpHeaders.ContentType, thumbnail.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${thumbnail.name}\"")
                            })
                        }

                        description?.let { append("description", it) }
                        focus?.let { append("focus", it) }
                    },
                )
            )

            var currentProgress = 0f
            var realProgress = 0f

            onUpload { sendTotal: Long, contentLength: Long ->
                realProgress = sendTotal.toFloat() / contentLength.toFloat()
                if (sendTotal == contentLength || realProgress >= currentProgress + PROGRESS_STEP) {
                    currentProgress += PROGRESS_STEP
                    if (currentProgress > 1f) currentProgress = 1f
                    onUpload.invoke(currentProgress)
                }
            }
        }

    override suspend fun getFile(id: String): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v1/media/$id",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        )

    override suspend fun updateFile(id: String, thumbnail: PlatformFileWrapper?, description: String?, focus: String?): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v1/media/$id",
            requestMethod = HttpMethod.Put,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            val thumbnailData = thumbnail?.readBytes?.invoke()

            setBody(
                MultiPartFormDataContent(
                    formData {
                        if (thumbnail != null && thumbnailData != null) {
                            append("thumbnail", thumbnailData, Headers.build {
                                append(HttpHeaders.ContentType, thumbnail.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${thumbnail.name}\"")
                            })
                        }

                        description?.let { append("description", it) }
                        focus?.let { append("focus", it) }
                    },
                )
            )
        }

    override suspend fun search(
        query: String,
        type: String,
        resolve: Boolean?,
        following: Boolean?,
        accountId: String?,
        excludeUnreviewed: Boolean?,
        minId: String?,
        maxId: String?,
        limit: Int?,
        offset: Int?,
    ): Search =
        doRequest<SearchResponse, Search>(
            requestUrl = "$instanceUrl/api/v2/search",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = SearchMapper::map,
        ) {
            url {
                with(parameters) {
                    append("q", query)
                    append("type", type)

                    resolve?.let { append("resolve", "$it") }
                    following?.let { append("following", "$it") }
                    accountId?.let { append("account_id", it) }
                    excludeUnreviewed?.let { append("exclude_unreviewed", "$it") }
                    minId?.let { append("minId", it) }
                    maxId?.let { append("maxId", it) }
                    limit?.let { append("limit", "$it") }
                    offset?.let { append("offset", "$it") }
                }
            }
        }

    override suspend fun sendStatus(bundle: NewStatusBundle): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        ) {
            headers {
                append("Idempotency-Key", "${bundle.hashCode()}")
            }

            contentType(ContentType.Application.Json)

            setBody(
                CreateStatusRequest(
                    status = bundle.status,
                    mediaIds = bundle.mediaIds.takeIf { it.isNotEmpty() },
                    poll = if (bundle.pollOptions.isNotEmpty()) {
                        CreateStatusRequestPoll(
                            options = bundle.pollOptions,
                            expiresIn = bundle.pollExpiresIn,
                            multiple = bundle.pollAllowMultiple,
                            hideTotals = bundle.pollHideTotals,
                        )
                    } else {
                        null
                    },
                    inReplyToId = null,
                    sensitive = bundle.sensitive,
                    spoilerText = bundle.spoilerText.takeIf { it.isNotEmpty() },
                    visibility = bundle.visibility.name.lowercase(),
                    language = bundle.language,
                    scheduledAt = null,
                )
            )
        }

    override suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus =
        doRequest<ScheduledStatusResponse, ScheduledStatus>(
            requestUrl = "$instanceUrl/api/v1/statuses",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = ScheduledStatusMapper::map,
        ) {
            val scheduledDate = DateTimeUtils.getDateTimeFromPickers(bundle.scheduledAtDate, bundle.scheduledAtHour, bundle.scheduledAtHour)
            val convertedDate = DateTimeUtils.getAsDateTimeUTC(scheduledDate).format(LocalDateTime.Formats.ISO)

            headers {
                append("Idempotency-Key", "${bundle.hashCode()}")
            }

            contentType(ContentType.Application.Json)

            setBody(
                CreateStatusRequest(
                    status = bundle.status,
                    mediaIds = bundle.mediaIds.takeIf { it.isNotEmpty() },
                    poll = if (bundle.pollOptions.isNotEmpty()) {
                        CreateStatusRequestPoll(
                            options = bundle.pollOptions,
                            expiresIn = bundle.pollExpiresIn,
                            multiple = bundle.pollAllowMultiple,
                            hideTotals = bundle.pollHideTotals,
                        )
                    } else {
                        null
                    },
                    inReplyToId = null,
                    sensitive = bundle.sensitive,
                    spoilerText = bundle.spoilerText.takeIf { it.isNotEmpty() },
                    visibility = bundle.visibility.name.lowercase(),
                    language = bundle.language,
                    scheduledAt = convertedDate,
                )
            )
        }

    companion object {
        const val PROGRESS_STEP = 0.05f
    }
}
