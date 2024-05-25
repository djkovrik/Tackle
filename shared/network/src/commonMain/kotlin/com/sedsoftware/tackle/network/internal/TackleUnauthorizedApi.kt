package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.request.ApplicationRequest
import com.sedsoftware.tackle.network.request.TokenRequest
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.network.response.TokenDetails
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class TackleUnauthorizedApi(
    private val domainProvider: () -> String,
) : UnauthorizedApi {

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

    override suspend fun getServerInfo(url: String): InstanceDetails =
        doGet<InstanceDetails>(url = "$url/api/v2/instance")

    override suspend fun createApp(client: String, uri: String, scopes: String, website: String): ApplicationDetails =
        doPost<ApplicationDetails>(
            url = "$instanceUrl/api/v1/apps",
            body = ApplicationRequest(
                clientName = client,
                redirectUris = uri,
                scopes = scopes,
                website = website
            )
        )

    override suspend fun obtainToken(id: String, secret: String, uri: String, grantType: String): TokenDetails =
        doPost<TokenDetails>(
            url = "$instanceUrl/oauth/token",
            body = TokenRequest(
                clientId = id,
                clientSecret = secret,
                redirectUri = uri,
                grantType = GRANT_TYPE,
                scopes = DEFAULT_SCOPES,
            )
        )

    @Throws(Exception::class)
    private suspend inline fun <reified T> TackleUnauthorizedApi.doGet(url: String): T =
        httpClient
            .get(url) {
                headers {
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, Constants.USER_AGENT)
                }
            }
            .body()

    private suspend inline fun <reified T> TackleUnauthorizedApi.doPost(url: String, body: Any): T =
        httpClient
            .post(url) {
                headers {
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, Constants.USER_AGENT)
                }
                contentType(Application.Json)
                setBody(body)
            }
            .body()

    private companion object {
        const val GRANT_TYPE = "client_credentials"
        const val DEFAULT_SCOPES = "read write push"
    }
}
