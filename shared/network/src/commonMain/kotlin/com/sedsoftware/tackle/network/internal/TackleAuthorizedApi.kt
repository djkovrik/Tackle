package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.network.mappers.AccountMapper
import com.sedsoftware.tackle.network.mappers.MediaAttachmentMapper
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

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
        focus: String?
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
                val progress: Int = (bytesSentTotal / contentLength).toInt() * 100
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
}
