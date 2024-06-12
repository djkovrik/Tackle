package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.response.ApplicationDetails
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class TackleAuthorizedApi(
    private val domainProvider: () -> String,
    private val tokenProvider: () -> String,
) : AuthorizedApi {

    private val appToken: String
        get() = tokenProvider.invoke()

    private val instanceUrl: String
        get() = domainProvider.invoke()

    private val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    override suspend fun verifyCredentials(): ApplicationDetails =
        doGet<ApplicationDetails>(url = "$instanceUrl/api/v1/apps/verify_credentials")

    @Throws(Exception::class)
    private suspend inline fun <reified T> TackleAuthorizedApi.doGet(url: String): T =
        httpClient
            .get(url) {
                headers {
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, Constants.USER_AGENT)
                    append(HttpHeaders.Authorization, "${Constants.BEARER} $appToken")
                }
            }
            .body()
}
