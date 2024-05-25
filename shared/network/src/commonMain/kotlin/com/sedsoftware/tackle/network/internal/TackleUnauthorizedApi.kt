package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.request.ApplicationRequest
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
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

internal class TackleUnauthorizedApi : UnauthorizedApi {

    internal val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    override suspend fun getServerInfo(url: String): InstanceDetails =
        doGet<InstanceDetails>("$url/api/v2/instance")

    override suspend fun createApp(url: String, client: String, uris: String, scopes: String, site: String): ApplicationDetails =
        doPost<ApplicationDetails>(
            url = "$url/api/v1/apps",
            body = ApplicationRequest(client, uris, scopes, site)
        )

    @Throws(Exception::class)
    internal suspend inline fun <reified T> TackleUnauthorizedApi.doGet(url: String): T =
        httpClient
            .get(url) {
                headers {
                    append(HttpHeaders.Accept, "*/*")
                    append(HttpHeaders.UserAgent, Constants.USER_AGENT)
                }
            }
            .body()

    internal suspend inline fun <reified T> TackleUnauthorizedApi.doPost(url: String, body: Any): T =
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
}
