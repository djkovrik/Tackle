package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.network.response.RemoteErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal abstract class BaseApi(
    private val domainProvider: () -> String,
    private val tokenProvider: () -> String,
) {

    protected val appToken: String
        get() = tokenProvider.invoke()

    protected val instanceUrl: String
        get() = domainProvider.invoke()

    protected val json: Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
    }

    protected val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout)
    }

    @Throws(Exception::class)
    @Suppress("ThrowsCount", "SwallowedException")
    protected suspend inline fun <reified From, To> BaseApi.doRequest(
        requestUrl: String,
        requestMethod: HttpMethod,
        authenticated: Boolean,
        responseMapper: (From) -> To,
        additionalConfig: HttpRequestBuilder.() -> Unit = {},
    ): To {
        try {

            val response: HttpResponse = httpClient.request(requestUrl) {

                method = requestMethod

                headers {
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, Constants.USER_AGENT)

                    if (authenticated) {
                        append(HttpHeaders.Authorization, "${Constants.BEARER} $appToken")
                    }
                }

                additionalConfig.invoke(this)
            }

            if (!response.status.isSuccess()) {
                val errorDetails: RemoteErrorResponse? = response.remoteErrorDetails()
                throw TackleException.RemoteServerException(
                    description = errorDetails?.errorDescription,
                    code = response.status.value,
                    message = errorDetails?.error,
                )
            }

            return responseMapper(json.decodeFromString<From>(response.body()))
        } catch (exception: SerializationException) {
            throw TackleException.SerializationException(exception)
        } catch (exception: IOException) {
            throw TackleException.NetworkException(exception)
        } catch (exception: ServerResponseException) {
            throw TackleException.NetworkException(exception)
        } catch (exception: ClientRequestException) {
            throw TackleException.NetworkException(exception)
        }
    }

    @Suppress("SwallowedException")
    suspend inline fun HttpResponse.remoteErrorDetails(): RemoteErrorResponse? =
        try {
            json.decodeFromString<RemoteErrorResponse>(body())
        } catch (e: SerializationException) {
            null
        }

    internal object Constants {
        const val USER_AGENT = "Tackle"
        const val BEARER = "Bearer"
        const val GRANT_TYPE = "authorization_code"
    }
}
