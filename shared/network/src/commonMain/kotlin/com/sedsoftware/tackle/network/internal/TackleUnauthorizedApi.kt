package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.network.model.Token
import com.sedsoftware.tackle.network.request.ApplicationRequest
import com.sedsoftware.tackle.network.request.TokenRequest
import com.sedsoftware.tackle.network.response.ApplicationResponse
import com.sedsoftware.tackle.network.response.InstanceResponse
import com.sedsoftware.tackle.network.response.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application as ApplicationContentType
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

    override suspend fun getServerInfo(url: String): Instance {
        val response: InstanceResponse = doGet<InstanceResponse>(url = "$url/api/v2/instance")

        return Instance(
            domain = response.domain,
            title = response.title,
            version = response.version,
            sourceUrl = response.sourceUrl,
            description = response.description,
            activePerMonth = response.usage?.users?.activePerMonth ?: 0L,
            thumbnailUrl = response.thumbnail?.url.orEmpty(),
            languages = response.languages,
        )
    }

    override suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application {
        val response: ApplicationResponse = doPost<ApplicationResponse>(
            url = "$instanceUrl/api/v1/apps",
            body = ApplicationRequest(
                clientName = client,
                redirectUris = uri,
                scopes = scopes,
                website = website
            )
        )

        return Application(
            name = response.name,
            website = response.website,
            clientId = response.clientId,
            clientSecret = response.clientSecret,
        )
    }

    override suspend fun obtainToken(id: String, secret: String, code: String, uri: String, scopes: String): Token {
        val response = doPost<TokenResponse>(
            url = "$instanceUrl/oauth/token",
            body = TokenRequest(
                clientId = id,
                clientSecret = secret,
                code = code,
                redirectUri = uri,
                grantType = GRANT_TYPE,
                scopes = scopes,
            )
        )

        return Token(
            accessToken = response.accessToken,
            tokenType = response.tokenType,
            scope = response.scope,
            createdAt = response.createdAt,
        )
    }

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
                contentType(ApplicationContentType.Json)
                setBody(body)
            }
            .body()

    private companion object {
        const val GRANT_TYPE = "authorization_code"
    }
}
