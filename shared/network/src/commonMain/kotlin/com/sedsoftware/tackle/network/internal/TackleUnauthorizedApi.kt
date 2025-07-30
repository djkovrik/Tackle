package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Token
import com.sedsoftware.tackle.network.mapper.ApplicationMapper
import com.sedsoftware.tackle.network.mapper.CustomEmojiMapper
import com.sedsoftware.tackle.network.mapper.InstanceMapper
import com.sedsoftware.tackle.network.mapper.TokenMapper
import com.sedsoftware.tackle.network.request.ApplicationRequest
import com.sedsoftware.tackle.network.request.TokenRequest
import com.sedsoftware.tackle.network.response.ApplicationResponse
import com.sedsoftware.tackle.network.response.CustomEmojiResponse
import com.sedsoftware.tackle.network.response.InstanceResponse
import com.sedsoftware.tackle.network.response.TokenResponse
import com.sedsoftware.tackle.utils.extension.orZero
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlin.time.Duration.Companion.minutes

internal class TackleUnauthorizedApi(
    domainProvider: () -> String,
    tokenProvider: () -> String,
) : BaseApi(domainProvider = domainProvider, tokenProvider = tokenProvider), UnauthorizedApi {

    init {
        Napier.base(DebugAntilog())
    }

    override suspend fun getServerInfo(url: String): Instance =
        doRequest<InstanceResponse, Instance>(
            requestUrl = "$url/api/v2/instance",
            requestMethod = HttpMethod.Get,
            authenticated = false,
            responseMapper = InstanceMapper::map,
        )

    override suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application =
        doRequest<ApplicationResponse, Application>(
            requestUrl = "$instanceUrl/api/v1/apps",
            requestMethod = HttpMethod.Post,
            authenticated = false,
            responseMapper = ApplicationMapper::map,
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                ApplicationRequest(
                    clientName = client,
                    redirectUris = uri,
                    scopes = scopes,
                    website = website
                )
            )
        }


    override suspend fun obtainToken(id: String, secret: String, code: String, uri: String, scopes: String): Token =
        doRequest<TokenResponse, Token>(
            requestUrl = "$instanceUrl/oauth/token",
            requestMethod = HttpMethod.Post,
            authenticated = false,
            responseMapper = TokenMapper::map,
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                TokenRequest(
                    clientId = id,
                    clientSecret = secret,
                    code = code,
                    redirectUri = uri,
                    grantType = Constants.GRANT_TYPE,
                    scopes = scopes,
                )
            )
        }

    override suspend fun getServerEmojis(): List<CustomEmoji> =
        doRequest<List<CustomEmojiResponse>, List<CustomEmoji>>(
            requestUrl = "$instanceUrl/api/v1/custom_emojis",
            requestMethod = HttpMethod.Get,
            authenticated = false,
            responseMapper = CustomEmojiMapper::map,
        )

    override suspend fun downloadFile(url: String, onProgress: (Float) -> Unit): ByteArray {
        return httpClient.prepareGet(
            urlString = url,
            block = {
                val timeout = 10.minutes.inWholeMilliseconds
                timeout {
                    requestTimeoutMillis = timeout
                    connectTimeoutMillis = timeout
                    socketTimeoutMillis = timeout
                }

                onDownload { bytesReceived: Long, contentLength: Long? ->
                    val received = bytesReceived.toFloat()
                    val length = contentLength?.toFloat()
                    val progress = length?.let { received / it } ?: 0f
                    Napier.d(tag = "network_debug", message = "Received $received bytes from total $length")
                    onProgress(progress)
                }
            },
        ).execute { response: HttpResponse ->
            if (!response.status.isSuccess()) {
                throw TackleException.RemoteServerException(
                    message = response.remoteErrorDetails()?.error,
                    description = response.remoteErrorDetails()?.errorDescription,
                    code = response.status.value,
                )
            }

            val byteReadChannel: ByteReadChannel = response.bodyAsChannel()
            val contentLength = response.contentLength()?.toInt().orZero()
            if (contentLength == 0) {
                throw TackleException.DownloadableFileEmpty
            }

            val result = ByteArray(contentLength)
            var offset = 0

            do {
                val currentRead = byteReadChannel.readAvailable(result, offset, result.size)
                Napier.d(tag = "network_debug", message = "Read $currentRead bytes from total $contentLength")
                offset += currentRead
            } while (currentRead > 0)

            return@execute result
        }
    }
}
