package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.response.RemoteErrorResponse
import com.sedsoftware.tackle.utils.RemoteServerException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal abstract class BaseApi(
    private val domainProvider: () -> String,
    private val tokenProvider: () -> String,
) {

    protected val appToken: String
        get() = tokenProvider.invoke()

    protected val instanceUrl: String
        get() = domainProvider.invoke()

    private val json: Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
    }

    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) { json(json) }
    }

    @Throws(Exception::class)
    protected suspend inline fun <reified From, To> BaseApi.doRequest(
        requestUrl: String,
        requestMethod: HttpMethod,
        authenticated: Boolean,
        responseMapper: (From) -> To,
        additionalConfig: HttpRequestBuilder.() -> Unit = {},
    ): To {

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
            val errorBody: RemoteErrorResponse = json.decodeFromString(response.body())
            throw RemoteServerException(errorBody.error, errorBody.errorDescription, response.status.value)
        }

        return responseMapper(json.decodeFromString<From>(response.body()))
    }

    internal object Constants {
        const val USER_AGENT = "Tackle"
        const val BEARER = "Bearer"
        const val GRANT_TYPE = "authorization_code"
    }
}
