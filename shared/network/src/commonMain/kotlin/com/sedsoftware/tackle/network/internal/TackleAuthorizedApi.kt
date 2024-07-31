package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.network.mappers.AccountMapper
import com.sedsoftware.tackle.network.mappers.MediaAttachmentMapper
import com.sedsoftware.tackle.network.mappers.SearchMapper
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import com.sedsoftware.tackle.network.response.SearchResponse
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.parameters

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
        onUpload: (Int) -> Unit,
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
            onUpload { bytesSentTotal: Long, contentLength: Long ->
                val progress: Int = (bytesSentTotal / contentLength).toInt() * ONE_HUNDRED_PERCENTS
                onUpload.invoke(progress)
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
            parameters {
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

    companion object {
        const val ONE_HUNDRED_PERCENTS = 100
    }
}
