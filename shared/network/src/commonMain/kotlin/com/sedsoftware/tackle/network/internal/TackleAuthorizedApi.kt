package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.FileWrapper
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
        file: FileWrapper,
        onUpload: (Int) -> Unit,
        thumbnail: FileWrapper?,
        description: String?,
        focus: String?
    ): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v2/media",
            requestMethod = HttpMethod.Post,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", file.data, Headers.build {
                            append(HttpHeaders.ContentType, file.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                        })

                        thumbnail?.let {
                            append("thumbnail", it.data, Headers.build {
                                append(HttpHeaders.ContentType, it.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${it.name}\"")
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

    override suspend fun updateFile(id: String, thumbnail: FileWrapper?, description: String?, focus: String?): MediaAttachment =
        doRequest<MediaAttachmentResponse, MediaAttachment>(
            requestUrl = "$instanceUrl/api/v1/media/$id",
            requestMethod = HttpMethod.Put,
            authenticated = true,
            responseMapper = MediaAttachmentMapper::map,
        ) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        thumbnail?.let {
                            append("thumbnail", it.data, Headers.build {
                                append(HttpHeaders.ContentType, it.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${it.name}\"")
                            })
                        }

                        description?.let { append("description", it) }
                        focus?.let { append("focus", it) }
                    },
                )
            )
        }
}
