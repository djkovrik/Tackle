package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.api.AuthorizedApi
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
import com.sedsoftware.tackle.network.mapper.AccountMapper
import com.sedsoftware.tackle.network.mapper.MediaAttachmentMapper
import com.sedsoftware.tackle.network.mapper.PollMapper
import com.sedsoftware.tackle.network.mapper.ScheduledStatusMapper
import com.sedsoftware.tackle.network.mapper.SearchMapper
import com.sedsoftware.tackle.network.mapper.StatusMapper
import com.sedsoftware.tackle.network.mapper.TranslationMapper
import com.sedsoftware.tackle.network.request.CreateStatusRequest
import com.sedsoftware.tackle.network.request.CreateStatusRequestPoll
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import com.sedsoftware.tackle.network.response.PollResponse
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.network.response.SearchResponse
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.network.response.TranslationResponse
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
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.encodeToString

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

            onUpload { sendTotal: Long, contentLength: Long? ->
                realProgress = sendTotal.toFloat() / (contentLength?.toFloat() ?: 1f)
                if (sendTotal == contentLength || realProgress >= currentProgress + PROGRESS_STEP) {
                    currentProgress += PROGRESS_STEP
                    if (currentProgress > 1f) currentProgress = 1f
                    onUpload.invoke(currentProgress)
                }
            }
        }

    override suspend fun getFile(id: String): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v1/media",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            url {
                appendPathSegments(id)
            }
        }

    override suspend fun updateFile(id: String, thumbnail: PlatformFileWrapper?, description: String?, focus: String?): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v1/media",
            requestMethod = HttpMethod.Put,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            val thumbnailData = thumbnail?.readBytes?.invoke()

            url {
                appendPathSegments(id)
            }

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

    override suspend fun search(bundle: SearchRequestBundle): Search =
        doRequest<SearchResponse, Search>(
            requestUrl = "$instanceUrl/api/v2/search",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = SearchMapper::map,
        ) {
            url {
                with(parameters) {
                    append("q", bundle.query)
                    append("type", bundle.type)

                    bundle.resolve?.let { append("resolve", "$it") }
                    bundle.following?.let { append("following", "$it") }
                    bundle.accountId?.let { append("account_id", it) }
                    bundle.excludeUnreviewed?.let { append("exclude_unreviewed", "$it") }
                    bundle.minId?.let { append("minId", it) }
                    bundle.maxId?.let { append("maxId", it) }
                    bundle.limit?.let { append("limit", "$it") }
                    bundle.offset?.let { append("offset", "$it") }
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
            val scheduledAt = DateTimeUtils.getDateTimeFromPickers(bundle.scheduledAtDate, bundle.scheduledAtHour, bundle.scheduledAtMinute)
            val convertedDate = DateTimeUtils.getAsDateTimeUTC(scheduledAt).format(LocalDateTime.Formats.ISO)

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

    override suspend fun deleteStatus(id: String, deleteMedia: Boolean): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses",
            requestMethod = HttpMethod.Delete,
            authenticated = true,
            responseMapper = StatusMapper::map,
        ) {
            url {
                appendPathSegments(id)

                parameters.append("delete_media", "$deleteMedia")
            }
        }

    override suspend fun translateStatus(id: String, lang: String): Translation =
        doRequest<TranslationResponse, Translation>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/translate",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = TranslationMapper::map,
        ) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("lang", lang)
                    }
                )
            )
        }

    override suspend fun favouriteStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/favourite",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun unfavouriteStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/unfavourite",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun boostStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/:$id/reblog",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun unboostStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/unreblog",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun bookmarkStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/bookmark",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun unbookmarkStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/unbookmark",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun pinStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/pin",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun unpinStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/unpin",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun muteStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/mute",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun unmuteStatus(id: String): Status =
        doRequest<StatusResponse, Status>(
            requestUrl = "$instanceUrl/api/v1/statuses/$id/unmute",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = StatusMapper::map,
        )

    override suspend fun votePoll(id: String, choices: List<Int>): Poll =
        doRequest<PollResponse, Poll>(
            requestUrl = "$instanceUrl/api/v1/polls/$id/votes",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = PollMapper::map,
        ) {
            contentType(ContentType.Application.Json)

            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("choices", json.encodeToString(choices))
                    }
                )
            )
        }

    companion object {
        const val PROGRESS_STEP = 0.05f
    }
}
